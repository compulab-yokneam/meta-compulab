#!/bin/bash

[[ -n ${__CL_FUNCTIONS__} ]] && return || export __CL_FUNCTIONS__='__CL_FUNCTIONS__'

set -a

WORK=/usr/local/bin/cl-deploy

prepare_fs() {

[[ $(who -r | wc -l) = 0 ]] || return

mount -t proc proc /proc
mount -t sysfs sys /sys
mount -t tmpfs tmp /run
mkdir -p /run/systemd

post_main() {
        reboot_cpl
}
}

pre_main() {

dmesg -D
prepare_fs
mount / -o remount,rw

}

post_main() {

true

}

error_exit() {
cat << eof
${1}
eof
exit $2
}

get_block_device() {
[[ -b ${1} ]] || return
edev=${1}
while :
do
eval $(udevadm info --query=property -n ${edev} | awk '/DEVPATH|DEVNAME|DEVTYPE/')
[[ ${DEVTYPE} == 'disk' ]] && break
edev=$(basename $(dirname ${DEVPATH}))
done
echo ${DEVNAME}
}

get_root_part() {
root_part=$(findmnt / -n -o source)
echo ${root_part}
}

get_root_device() {
echo $(get_block_device $(get_root_part))
}

get_part_by_number() {
# 1st -- device
# 2nd -- partition

local dev_name=${1}
local part_num=${2}

declare -A boot_id=( [mm]=mmc [sd]=usb )
declare -A part_id=( [mm]=p [sd]='' [nv]=p [lo]=p )

if [[ ! -b ${dev_name} ]];then
error_exit "Invalid destination device ${dev_name}" 4
fi

local partid=${dev_name:5:2}
local part=${dev_name}${part_id[${partid}]}${part_num}

if [[ ! -b ${part} ]];then
error_exit "Invalid partition ${part} device." 6
fi

echo ${part}
}

reboot_cpl () {
  sync;sync;sync
  for cmd in s u b;do
  echo ${cmd} > /proc/sysrq-trigger
  done
}

sync_cpl () {
return
  sync;sync;sync
  for cmd in s u;do
  echo ${cmd} > /proc/sysrq-trigger
  done
}

poweroff_cpl () {
  sync;sync;sync
  for cmd in s u o;do
  echo ${cmd} > /proc/sysrq-trigger
  done
}

declare -x dry_run="no"
declare -xi dry_run_res=0

line() { local len=${1:-60}; printf '%*s\n' ${len} '' | tr ' ' - ; }

if [[ $(who -r) ]];then
exec 5> /tmp/$(basename "$0").$$.log
BASH_XTRACEFD="5"
fi
set -x
