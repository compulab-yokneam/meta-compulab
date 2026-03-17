#!/bin/bash

set -x

uid=$(id -u)
[[ ${uid} -eq 0 ]] || exit -13

function restore_partclone_func() {
    [[ -n ${device:-""} ]] || exit -2
    [[ -b ${device:-""} ]] || exit -3

    local devname=$(basename ${device})
    declare -A p_delimmeter=( ['sd']='p' ['lo']='p' ['mm']='p' ['nv']='p' )
    declare -A i_command=( ['xz']='xz -dc ' )
    local p=${p_delimmeter[${devname:0:2}]:-""}

    if [[ ! -f ${src}/disk.layout ]];then
        echo "Error: disk.layout is missing ..."
        return 2
    fi

    # Create layout

    local target=${device}

    cat ${src}/disk.layout | sfdisk ${target}
    echo "w" | fdisk ${target}
    local part_dev=$(sfdisk --list ${target} | awk -v t=${target}  '{ if ( $1 ~ t ) { print $2" "$1 } }' | sort -n | tail -1 | awk '$0=$2')
    local last_part_num=$(cat  /sys/class/block/$(basename ${part_dev})/partition)
    parted -s ${target} resizepart ${last_part_num} 100%

    sleep 1
    # Restore disk-id UUID if possible
    [[ ! -f ${src}/disk.id ]] && sfdisk --disk-id ${target} $(cat ${src}/disk.id)

    for _wait in $(seq 1 3);do
        echo -n $_wait" "; sleep 1
    done

    for _target in ${device}*;do
        eval $(blkid ${_target} | awk -F":" '($0="dev="$1" "$2)';)
        local _dev=$(basename ${dev})
        [[ -f /sys/class/block/${_dev}/partition ]] || continue
        local num=$(cat /sys/class/block/${_dev}/partition)
        local image=$(ls ${src}/part${num}.* 2>/dev/null || true)
        if [[ -z ${image:-""} ]];then
            echo "No image file for part # ${num}; skip 'n' continue ..."
            continue
        fi
        local name=$(basename ${image}); name=(${name//./ })
        local type=${name[1]} uuid=${name[2]}
        if [[ ${type} = swap ]];then
        # Create a swap partition
            mkswap ${_target} --uuid ${uuid}
            continue
        fi
        [[ ${type} = dd ]] && pc_command="partclone.dd" || pc_command="partclone.restore -C "
        local _file_type=$(file ${image} | awk '$0=tolower($2)')
        local _command=${i_command[${_file_type}]:-"dd if="}
        ${_command}${image} | ${pc_command} -d -s - -o ${_target} || rc=$?
        if [[ ${rc} -ne 0 ]];then
            echo "Error: deployment ( ${image} -> ${_target} )  error=${rc}"
            return ${rc}
        fi
        if [[ ${last_part_num} = ${num} ]];then
            e2fsck -f ${_target} || true
            resize2fs ${_target} || true
        fi
    done

    return 0
}

src=${src} device=${device} restore_partclone_func
