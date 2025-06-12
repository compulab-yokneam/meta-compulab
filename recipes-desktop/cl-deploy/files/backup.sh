#!/bin/bash

set -x

uid=$(id -u)
[[ ${uid} -eq 0 ]] || exit -13

[[ -n ${device:-""} ]] || exit 2
[[ -b ${device:-""} ]] || exit 3

sfdisk --dump ${device} | awk '!/last-lba|^device:/' > disk.layout
sfdisk --disk-id ${device} > disk.id

for _dev in ${device}*;do
    eval $(blkid $_dev | awk -F":" '($0="dev="$1" "$2)';)
    _dev=$(basename ${dev})
    _part=part${_dev: -1}
    LABEL=${LABEL-"${_part}"}
    [[ -f /sys/class/block/${_dev}/partition ]] && true || continue
    if [[ ${TYPE} = swap ]];then
        touch ${_part}.${TYPE}.${UUID}.${LABEL}.tar.bz2
    else
        mkdir -p /tmp/${dev}
        TARGET=$(findmnt ${dev} --output TARGET --noheadings || true)
        [[ -n ${TARGET} ]] && mount -o ro -B ${TARGET} /tmp/${dev} || mount -o ro ${dev} /tmp/${dev}
        tar -C /tmp/${dev} -cjf - . | pv -N "backup ${dev}" | dd of=${_part}.${TYPE}.${UUID}.${LABEL}.tar.bz2
        umount /tmp/${dev}
        rm -rf /tmp/${dev}
    fi
done
