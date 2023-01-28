systemd_resolved_fix() {
    if [ -L ${IMAGE_ROOTFS}/etc/systemd/system/multi-user.target.wants/systemd-resolved.service ];then
        if [ -f ${IMAGE_ROOTFS}/etc/resolv-conf.systemd ];then
            ln -sf /etc/resolv-conf.systemd ${IMAGE_ROOTFS}/etc/resolv.conf
        fi
    fi
    ln -sf /etc/resolv-conf.systemd ${IMAGE_ROOTFS}/etc/resolv.conf
}
