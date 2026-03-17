#!/bin/bash -e

set -x

uid=$(id -u)
[[ ${uid} -eq 0 ]] || exit -13

declare -A pc_options=( ['ext4']="-c" ['vfat']="-c" )

[[ -n ${device:-""} ]] || exit 2
[[ -b ${device:-""} ]] || exit 3

function backup_partclone_func() {
    sfdisk --dump ${device} | awk '!/last-lba|^device:/' > disk.layout
    sfdisk --disk-id ${device} > disk.id
    declare -A pc_options=( ['ext4']="-c" ['vfat']="-c" )
    declare -A cs_options=( ['xz']=".xz -x 'xz -9'" ['zstd']=".zst -x 'zstd -v -T0 --adapt --long'" )
    local filter=${cs_options[${compress}]:-""}

    for _dev in ${device}*;do
        unset TYPE pc_opt
        eval $(blkid $_dev | awk -F":" '($0="dev="$1" "$2)';)
        TYPE=${TYPE:-"dd"}
        _dev=$(basename ${dev})
        local pc_opt=${pc_options[${TYPE}]:-""}
        [[ -f /sys/class/block/${_dev}/partition ]] && true || continue
        _part="part"$(cat /sys/class/block/${_dev}/partition)
        if [[ ${TYPE} = swap ]];then
            touch ${_part}.${TYPE}.${UUID}.partclone.xz
        else
            partclone.${TYPE} -F ${pc_opt} -s ${dev} -o ${_part}.${TYPE}.${UUID}.partclone${filter}
        fi
    done
}

compress=${compress:-"none"} device=${device} backup_partclone_func
