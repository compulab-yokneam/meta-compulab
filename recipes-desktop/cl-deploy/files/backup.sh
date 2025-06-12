#!/bin/bash -e

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
	[[ -f /sys/class/block/${_dev}/partition ]] && true || continue
	if [[ ${TYPE} = swap ]];then
		touch ${_part}.${TYPE}.${UUID}.partclone.lz4
	else
		partclone.${TYPE} -F -c -s ${dev} -o - | lz4c -c9 - > ${_part}.${TYPE}.${UUID}.partclone.lz4
	fi
done
