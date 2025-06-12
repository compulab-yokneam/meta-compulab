#!/bin/bash

set -x

uid=$(id -u)
[[ ${uid} -eq 0 ]] || exit -13

deploy_layout() {
    [[ -n ${device:-""} ]] || exit -2
    [[ -b ${device:-""} ]] || exit -3

    devname=$(basename ${device})
    [[ ${devname:0:2} = "sd" ]] && p=""
    [[ ${devname:0:2} = "lo" ]] && p="p"
    [[ ${devname:0:2} = "mm" ]] && p="p"

    if [[ ! -f ${src}/disk.layout ]];then
        echo "Error: disk.layout is missing ..."
        return 2
    fi

    local target=${device}

    # Apply the disk layout
    cat ${src}/disk.layout | sfdisk ${target}
    partprobe ${target}
    local part_dev=$(sfdisk --list ${target} | awk 'END { print $1 }')
    local part_num=${part_dev#${target}${p}}
    parted -s ${target} resizepart ${part_num} 100%

    # Restore disk-id UUID if possible
    [[ ! -f ${src}/disk.id ]] || sfdisk --disk-id ${target} $(cat ${src}/disk.id)

    # Copy data

    for image in ${src}/part*;do
    _image=$(basename ${image})
        name=(${_image//./ })
        num=${name[0]: -1}
        type=${name[1]}
        uuid=${name[2]}
        label=${name[3]}
        if [[ ${type} = swap ]];then
             # Create a swap partition
            mkswap ${target}${p}${num} --uuid ${uuid}
            continue
        fi
        [[ ${type} = "ext"* ]] && opt=" -F -L ${label} -U ${uuid} -t ${type}" || true
        [[ ${type} = "ext4"* ]] && opt+=" -O 64bit -O has_journal" || true
        mkfs.${type} ${opt} ${target}${p}${num}
        mkdir -p /tmp/${target}${p}${num}
        mount ${target}${p}${num} /tmp/${target}${p}${num}
        pv -N "${target}${p}${num}" ${image} | tar -C /tmp/${target}${p}${num} -xjf - 2>/dev/null
        umount ${target}${p}${num}
        rm -rf /tmp/${target}${p}${num}
    done
    sync;sync;sync
    return 0
}

src=${src} device=${device} deploy_layout

cat << eof
--- Restored device ${device} ---

$(blkid ${device}*)

---
eof
