#!/bin/bash

set -e

# All sizes are in blocks of 512 bytes

root_device=$(basename $(findmnt / --noheadings --output SOURCE))
part2resize=${part2resize:-${root_device: -1}}

LINUX_STORAGE_DEVICE=/dev/$(basename $(dirname /sys/block/*/${root_device}))
LINUX_ROOT_PART=/dev/${root_device:0:-1}${part2resize}

linux_storage_root_device_base=$(basename ${LINUX_STORAGE_DEVICE})
linux_data_part_base=$(basename ${LINUX_ROOT_PART})

total_disk_size=$(cat /sys/block/${linux_storage_root_device_base}/size)

data_part_size=$(cat /sys/block/${linux_storage_root_device_base}/${linux_data_part_base}/size)
data_part_start=$(cat /sys/block/${linux_storage_root_device_base}/${linux_data_part_base}/start)

if [ -z "${total_disk_size}" ] || [ -z "${data_part_size}" ] || [ -z "${data_part_start}" ]; then
    echo "Failed to read disk sizes. Aborting..."
    exit 1
fi

data_part_end=$(expr ${data_part_start} + ${data_part_size})

# expr returns 1 (error) if the result is 0, which terminates the script
# because of 'set -e'. Silence this error
free_space=$(expr ${total_disk_size} - ${data_part_end} || true)

# If there is less than 8196 blocks = 4 MiB free unused space, we consider
# the disk as already resized. After resizing, some disk space may still
# have been left unused.
if [ ${free_space} -lt 8196 ]; then
    echo "Disk has already been resized."
    exit 0
fi

# Parted will refuse to resize the parition because it needs to re-write the
# partition table and will refuse to do so unless there is a backup. This
# ensures that GPT backup headers are written to the end of the disk.
# Note: On some MBR systems using BusyBox's fdisk this call will fail,
# this is OK (MBR systems don't have a backup header), always return true. 
echo "w" | fdisk ${LINUX_STORAGE_DEVICE} &> /dev/null || true

# parted -s ${LINUX_STORAGE_DEVICE} resizepart ${part2resize} 100%
echo "Yes" | parted ---pretend-input-tty ${LINUX_STORAGE_DEVICE} resizepart ${part2resize} 100% || true
resize2fs /dev/${root_device}

/lib/systemd/systemd-growfs /
