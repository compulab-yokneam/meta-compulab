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
mkdir -p ${mpoint}/boot/efi
mount ${boot_dev} ${mpoint}/boot/efi
mount -B /dev  ${mpoint}/dev
mount -B /proc ${mpoint}/proc
mount -B /sys ${mpoint}/sys

CR_CONF=/tmp/grub-mkconfig.sh
MK_CONF=${mpoint}/${CR_CONF}

grub-editenv ${mpoint}/boot/grub/grubenv create
cat << eof | tee /dev/null ${MK_CONF}
#!/bin/bash -x

cd /boot
for image in Image-*;do
ln -sf \${image} kernel-\${image/Image-/};
done
cd -
grub-mkconfig -o /boot/grub/grub.cfg
grub-mkconfig -o /boot/grub/grub.cfg
command cl-grub-mkimage 2>/dev/null || true
eof

chmod a+x ${MK_CONF}
chroot ${mpoint} ${CR_CONF}

umount ${mpoint}/sys
umount ${mpoint}/proc
umount ${mpoint}/dev
# Remove grub folder from the boot partition
rm -rf ${mpoint}/boot/efi/grub
umount ${mpoint}/boot/efi
umount ${mpoint}
}

post_deploy
