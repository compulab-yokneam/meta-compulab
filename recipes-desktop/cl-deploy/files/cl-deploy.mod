#!/bin/bash

function part1_mod() {
file=${1}
pdev=$( stat --format=%n ${DST}*2 )
eval $(blkid ${pdev} | awk -F":" '($0=$2)')
sed -i "s/\(^UUID\)=.*/\1=${UUID}/g;s/\(^PARTUUID\).*$/\1=${PARTUUID}/g" ${file}
}

function part23_mod() {
file=${1}
pdev=$( stat --format=%n ${DST}*1 )
eval $(blkid ${pdev} | awk -F":" '($0=$2)')
sed -i "/^PARTUUID/d;" ${file}
cat << eof >> ${file}
PARTUUID=${PARTUUID}    /boot   auto    defaults,sync   0   2
eof
pdev=$( stat --format=%n ${DST}*${2} )
eval $(blkid ${pdev} | awk -F":" '($0=$2)')
cat << eof >> ${file}
PARTUUID=${PARTUUID}    /   ${TYPE} errors=remount-ro   0   1
eof
}

function part2_mod() {
part23_mod ${1} 2
}

function part3_mod() {
part23_mod ${1} 3
}

function post_deploy() {

mpoint=$(mktemp --dry-run)
src=$(basename ${SRC})
dst=$(basename ${DST})

declare -A partdl=( [mmc]="p" [loo]="p" )
_p=${partdl[${src:0:3}]}
p=${partdl[${dst:0:3}]}

declare -A devarr=( [1]="/EFI/BOOT/grub.cfg" [2]="/etc/fstab" [3]="/etc/fstab" )
declare -A modarr=( [1]="part1_mod" [2]="part2_mod" [3]="part3_mod" )
declare -A artarr=( [1]="grub-editenv ${mpoint}/EFI/BOOT/grubenv create" )

for d in ${!devarr[@]};do

    _dev=/dev/${dst}${p}${d}

    if [ -b ${_dev} ];then

        mkdir -p ${mpoint}

        mount ${_dev} ${mpoint}

        for _file in ${devarr[${d}]};do

            if [ -f ${mpoint}/${_file} ];then
                [[ -b ${src} ]] && sed -i "s/${src}${_p}/${dst}${p}/" ${mpoint}/${_file} || true
                command=${modarr[${d}]}
                [[ -n "${command}" ]] && ${command} ${mpoint}/${_file}
            fi

        done

        command=${artarr[${d}]}
        [[ -n "${command}" ]] && ${command}

        umount -l ${_dev}

        rm -rf ${mpoint}

    fi

done

}

function update_uuid() {
for dev in $(stat --format=%n ${DST}* ); do
	dev=/sys/class/block/$(basename ${dev})
	[[ -e ${dev}/partition ]] && sfdisk --part-uuid ${DST} $(cat ${dev}/partition) $(uuidgen -t)
done
}

[[ -n ${UUU} ]] && update_uuid
post_deploy
