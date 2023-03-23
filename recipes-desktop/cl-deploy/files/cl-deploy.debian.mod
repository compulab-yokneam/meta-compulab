function post_deploy() {

mpoint=$(mktemp --dry-run)
src=$(basename ${SRC})
dst=$(basename ${DST})

declare -A partdl=( [mmc]="p" [loo]="p" )
p=${partdl[${dst:0:3}]}

boot_dev=/dev/${dst}${p}1
root_dev=/dev/${dst}${p}2

mkdir -p ${mpoint}

mount ${root_dev} ${mpoint}
mount ${boot_dev} ${mpoint}/boot/efi
mount -B /dev  ${mpoint}/dev
mount -B /proc ${mpoint}/proc
mount -B /sys ${mpoint}/sys

CR_CONF=/tmp/grub-mkconfig.sh
MK_CONF=${mpoint}/${CR_CONF}

grub-editenv ${mpoint}/boot/efi/EFI/BOOT/grubenv create
cat << eof | tee /dev/null ${MK_CONF}
grub-mkconfig -o /boot/efi/EFI/BOOT/grub.cfg
grub-mkconfig -o /boot/efi/EFI/BOOT/grub.cfg
eof

chmod a+x ${MK_CONF}
chroot ${mpoint} ${CR_CONF}

umount ${mpoint}/sys
umount ${mpoint}/proc
umount ${mpoint}/dev
umount ${mpoint}/boot/efi
umount ${mpoint}
}

post_deploy
