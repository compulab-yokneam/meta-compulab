#!/bin/bash

function part1_mod() {
file=${1}
part=${2:-2}
pdev=$( stat --format=%n ${DST}*${part} )
eval $(blkid ${pdev} | awk -F":" '($0=$2)')
sed -i "s/\(^UUID\)=.*/\1=${UUID}/g;s/\(^PARTUUID\).*$/\1=${PARTUUID}/g" ${file}
}

declare -A rootmntarr=( [rw]="errors=remount-ro,noatime,rw" [ro]="ro" )

function part23_mod() {

local OPTIONS=${rootmntarr[${ROOTMODE}]}
[[ ${ROOTMODE} = "ro" ]] && local dry_run="#" || local dry_run=""

file=${1}
pdev=$( stat --format=%n ${DST}*1 )
eval $(blkid ${pdev} | awk -F":" '($0=$2)')
sed -i "/^PARTUUID/d;/ \/ /d;" ${file}
cat << eof >> ${file}
PARTUUID=${PARTUUID}    /boot   auto    defaults,sync   0   2
eof
pdev=$( stat --format=%n ${DST}*${2} )
eval $(blkid ${pdev} | awk -F":" '($0=$2)')
cat << eof >> ${file}
${dry_run}PARTUUID=${PARTUUID}    /   ${TYPE} ${OPTIONS}   0   1
eof
}

read_mostly_rootfs_hook () {
	# /etc/fstab modificatio
	local FSTAB=/etc/fstab
	if [ -e ${IMAGE_ROOTFS}/${FSTAB} ]; then
cat << eof >> ${IMAGE_ROOTFS}/${FSTAB}
tmpfs        /tmp tmpfs  defaults              0  0
tmpfs        /run tmpfs  mode=0755,nodev,nosuid,strictatime 0  0
# logs
tmpfs        /var/log tmpfs   nosuid,nodev         0       0
eof
	fi
	# journald.conf modification
	local JCONF=/etc/systemd/journald.conf
	[[ -e ${IMAGE_ROOTFS}/${JCONF} ]] && sed -i 's/.*\(Storage\).*/\1=volatile/g' ${IMAGE_ROOTFS}/${JCONF}
}

read_only_rootfs_hook () {
	# If we're using openssh and the /etc/ssh directory has no pre-generated keys,
	# we should configure openssh to use the configuration file /etc/ssh/sshd_config_readonly
	# and the keys under /var/run/ssh.
	if [ -d ${IMAGE_ROOTFS}/etc/ssh ]; then
		if [ -e ${IMAGE_ROOTFS}/etc/ssh/ssh_host_rsa_key ]; then
			echo "SYSCONFDIR=\${SYSCONFDIR:-/etc/ssh}" >> ${IMAGE_ROOTFS}/etc/default/ssh
			echo "SSHD_OPTS=" >> ${IMAGE_ROOTFS}/etc/default/ssh
		else
			echo "SYSCONFDIR=\${SYSCONFDIR:-/var/run/ssh}" >> ${IMAGE_ROOTFS}/etc/default/ssh
			echo "SSHD_OPTS='-f /etc/ssh/sshd_config_readonly'" >> ${IMAGE_ROOTFS}/etc/default/ssh
		fi
	fi

	# /etc/fstab modificatio
	local FSTAB=/etc/fstab
	if [ -e ${IMAGE_ROOTFS}/${FSTAB} ]; then
cat << eof >> ${IMAGE_ROOTFS}/${FSTAB}
tmpfs        /tmp tmpfs  defaults              0  0
tmpfs        /run tmpfs  mode=0755,nodev,nosuid,strictatime 0  0
# logs
tmpfs        /var/log tmpfs   nosuid,nodev         0       0
# lightdm
tmpfs        /var/lib/lightdm tmpfs   nosuid,nodev         0       0
# docker
tmpfs        /var/lib/docker tmpfs   nosuid,nodev         0       0
tmpfs        /var/lib/containerd tmpfs   nosuid,nodev         0       0
eof
	mkdir -p ${IMAGE_ROOTFS}/var/lib/docker ${IMAGE_ROOTFS}/var/lib/containerd ${IMAGE_ROOTFS}/var/lib/lightdm
	fi
	# journald.conf modification
	local JCONF=/etc/systemd/journald.conf
	[[ -e ${IMAGE_ROOTFS}/${JCONF} ]] && sed -i 's/.*\(Storage\).*/\1=volatile/g' ${IMAGE_ROOTFS}/${JCONF}


	# Postpone the listed services start to the moment they are granted RW disk operations
	for i in systemd-rfkill systemd-backlight@ systemd-tmpfiles-setup; do
		sed -e '/^After=/ {/local-fs.target/!s/^\(.*\)$/& local-fs.target/}' -i ${IMAGE_ROOTFS}/lib/systemd/system/${i}.service
	done

	# Create machine-id
	touch ${IMAGE_ROOTFS}/etc/machine-id
}

function part23_mod_ext() {
part23_mod ${1}/etc/fstab ${part}
part1_mod ${1}/boot/EFI/BOOT/grub.cfg ${part}

ischroot || return 0
# to take on ROOTMODE evaluation in chroot environment only
# while on the device leave it as is with all users' changes if any
[[ ${ROOTMODE} = "ro" ]] && IMAGE_ROOTFS=${1} read_only_rootfs_hook || IMAGE_ROOTFS=${1} read_mostly_rootfs_hook

}

function post_deploy() {

mpoint=$(mktemp --dry-run)
src=$(basename ${SRC})
dst=$(basename ${DST})

declare -A partdl=( [mmc]="p" [loo]="p" )
_p=${partdl[${src:0:3}]}
p=${partdl[${dst:0:3}]}

declare -A devarr=( [1]="/EFI/BOOT/grub.cfg" [2]="/" [3]="/" )
declare -A modarr=( [1]="part1_mod" [2]="part23_mod_ext" [3]="part23_mod_ext" )
declare -A artarr=( [1]="grub-editenv ${mpoint}/EFI/BOOT/grubenv create" )

for d in ${!devarr[@]};do

    _dev=/dev/${dst}${p}${d}

    if [ -b ${_dev} ];then

        mkdir -p ${mpoint}

        mount ${_dev} ${mpoint}

        for _file in ${devarr[${d}]};do

            if [ -e ${mpoint}/${_file} ];then
                [[ -b ${src} ]] && sed -i "s/${src}${_p}/${dst}${p}/" ${mpoint}/${_file} || true
                command=${modarr[${d}]}
                [[ -n "${command}" ]] && part=${d} ${command} ${mpoint}/${_file}
            fi

        done

        command=${artarr[${d}]}
        [[ -n "${command}" ]] && {
	${command} &>/dev/null || true
	}

        umount -l ${_dev}

        rm -rf ${mpoint}

    fi

done

}

function update_uuid() {
sfdisk --dump ${DST} | sed '/label-id/d' | sfdisk ${DST}
for i in  /sys/class/block/$(basename ${DST})*;do
[[ -f ${i}/partition ]] && sfdisk --part-uuid ${DST} $(cat ${i}/partition) $(uuidgen --md5 --time) || true
done
}

ROOTMODE=${ROOTMODE:-rw}
update_uuid
post_deploy
