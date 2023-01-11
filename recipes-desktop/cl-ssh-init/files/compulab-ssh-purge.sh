#!/bin/bash

rootfs=${rootfs:-"/"}

rm -f ${rootfs}etc/ssh/ssh_host_key ${rootfs}etc/ssh/ssh_host_key.pub
rm -f ${rootfs}etc/ssh/ssh_host_rsa_key ${rootfs}etc/ssh/ssh_host_rsa_key.pub
rm -f ${rootfs}etc/ssh/ssh_host_dsa_key ${rootfs}etc/ssh/ssh_host_dsa_key.pub
rm -f ${rootfs}etc/ssh/ssh_host_ecdsa_key ${rootfs}etc/ssh/ssh_host_ecdsa_key.pub
rm -f ${rootfs}etc/ssh/ssh_host_ed25519_key ${rootfs}etc/ssh/ssh_host_ed25519_key.pub
for ext in .ucf-new .ucf-old .ucf-dist ""; do
    rm -f "${rootfs}etc/ssh/sshd_config$ext"
done
rm -rf ${rootfs}etc/ssh/sshd_config.d
if which ucf >/dev/null 2>&1; then
    ucf --purge ${rootfs}etc/ssh/sshd_config
fi
if which ucfr >/dev/null 2>&1; then
    ucfr --purge openssh-server ${rootfs}etc/ssh/sshd_config
fi
rm -f ${rootfs}etc/ssh/sshd_not_to_be_run
[ ! -d ${rootfs}etc/ssh ] || rmdir --ignore-fail-on-non-empty ${rootfs}etc/ssh
