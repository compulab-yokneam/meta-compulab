enable_autodeployment() {
        cd ${IMAGE_ROOTFS}
        rm sbin/init
        ln -fs /usr/local/bin/cl-init sbin/init
        cd -
}
ROOTFS_POSTPROCESS_COMMAND += "${@bb.utils.contains('CL_DEPLOY_AUTO', '1', 'enable_autodeployment; ', ' ', d)}"
inherit core-image

