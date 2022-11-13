enable_autodeployment() {
	local dist_init='sbin/init'
	local save_init_d='usr/share/cl-deploy'
	local auto_init='usr/local/bin/cl-init'

        cd ${IMAGE_ROOTFS}
	cp -a ${dist_init} ${save_init_d}/
	rm ${dist_init}
        ln -fs /${auto_init} ${dist_init}
        cd -
}
ROOTFS_POSTPROCESS_COMMAND += "${@bb.utils.contains('CL_DEPLOY_AUTO', '1', 'enable_autodeployment; ', ' ', d)}"
inherit core-image

