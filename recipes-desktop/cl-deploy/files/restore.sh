#!/bin/bash

set -x

uid=$(id -u)
[[ ${uid} -eq 0 ]] || exit -13

deploy_layout() {
    [[ -n ${device:-""} ]] || exit -2
    [[ -b ${device:-""} ]] || exit -3

    devname=$(basename ${device})
    [[ ${devname:0:2} = "sd" ]] && p="" || p="p"
    [[ ${devname:0:2} = "lo" ]] && p="p" || p=""
    [[ ${devname:0:2} = "mm" ]] && p="p" || p=""

    if [[ ! -f ${src}/disk.layout ]];then
        echo "Error: disk.layout is missing ..."
        return 2
    fi

    # Create layout

    local target=${device}

    cat ${src}/disk.layout | sfdisk ${target}
    echo "w" | fdisk ${target}
    local part_dev=$(sfdisk --list ${target} | awk 'END { print $1 }')
    local part_num=${part_dev#${target}${p}}
    parted -s ${target} resizepart ${part_num} 100%

    # Restore disk-id UUID if possible
    [[ ! -f ${src}/disk.id ]] && sfdisk --disk-id ${target} $(cat ${src}/disk.id)

    # Copy data

    for image in ${src}/part*;do
	_image=$(basename ${image})
        name=(${_image//./ })
        num=${name[0]: -1}
        type=${name[1]}
        uuid=${name[2]}
        if [[ ${type} = swap ]];then
        # Create a swap partition
            mkswap ${target}${p}${num} --uuid ${uuid}
            continue
        fi
        lz4cat ${image} | partclone.restore -d -s - -o ${target}${p}${num} || rc=$?
        if [[ ${rc} -ne 0 ]];then
            echo "Error: deployment ( ${image} -> ${target}${p}${num} )  error=${rc}"
            return ${rc}
        fi
    done
    return 0
}

src=${src} device=${device} deploy_layout
