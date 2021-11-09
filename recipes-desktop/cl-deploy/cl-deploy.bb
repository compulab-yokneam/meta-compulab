# Simple recipe to add desktop icon and executable to run
# CompuLab Deployment Tool

DESCRIPTION = "CompuLab Deployment Tool"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=4a0e2a2916052068a420bbc50873f515"

PR = "r3"

SRC_URI = " \
	file://cl-deploy \
	file://cl-deploy.work \
	file://cl-deploy.helper \
	file://cl-deploy.layout \
	file://cl-deploy.mtd \
	file://cl-deploy.mod \
	file://cl-deploy.desktop \
	file://cl-deploy.png \
	file://cl-auto \
	file://cl-init \
	file://cl-reboot \
	file://cl-auto.conf.header \
	file://cl-auto.conf \
	file://cl-auto.bashrc \
	file://cl-auto.shell \
	file://cl-functions.inc \
	file://cl-auto.inc \
	file://cl-auto.notifier \
	file://00_cl-deploy.pre \
	file://00_cl-deploy.app \
	file://COPYING \
"

SRC_URI_append_cl-som-imx6ul = " \
	file://cl-deploy.cl-som-imx6ul \
"

S = "${WORKDIR}"

CL_DEPLOY_MOD = " ${@bb.utils.contains('BBFILE_COLLECTIONS', 'compulab-uefi', '1', '0', d)}"
# Some dsitro has a boot.src that evaluates whether the file rootfs://boot/auto exists and then
# add init=/usr/local/bin/cl-inti to the kernel bootargs, that turns the media
# into an autoinstaller.
CL_DEPLOY_BF = "${@bb.utils.contains('CL_DEPLOY_AUTO', '1', 'auto', 'no-auto', d)}"

do_install() {
	install -d ${D}${prefix}/local/bin
	install -d ${D}${datadir}/applications
	install -d ${D}${datadir}/cl-deploy
	install -d ${D}${datadir}/cl-deploy/pre
	install -d ${D}${datadir}/cl-deploy/app
	install -d ${D}${sysconfdir}


	install -m 0755 ${S}/cl-deploy ${D}${prefix}/local/bin/
	install -m 0755 ${S}/cl-deploy.work ${D}${prefix}/local/bin/
	if [ ${CL_DEPLOY_MOD} -eq 1 ];then
	install -m 0644 ${S}/cl-deploy.mod ${D}${prefix}/local/bin/
	fi
	install -m 0644 ${S}/cl-deploy.layout ${D}${prefix}/local/bin/
	install -m 0755 ${S}/cl-deploy.helper ${D}${prefix}/local/bin/
	install -m 0755 ${S}/cl-auto ${D}${prefix}/local/bin/
	install -m 0755 ${S}/cl-auto.shell ${D}${prefix}/local/bin/
	install -m 0755 ${S}/cl-init ${D}${prefix}/local/bin/
	install -m 0755 ${S}/cl-reboot ${D}${prefix}/local/bin/
	install -m 0644 ${S}/cl-deploy.png ${D}${datadir}/applications/
	install -m 0644 ${S}/cl-deploy.desktop ${D}${datadir}/applications/

	cat ${S}/cl-auto.conf.header ${S}/cl-auto.conf > ${S}/cl-auto.conf.sample

	install -m 0644 ${S}/cl-auto.conf.sample ${D}${datadir}/cl-deploy/
	install -m 0644 ${S}/cl-auto.conf.sample ${D}${sysconfdir}/cl-auto.conf
	install -m 0644 ${S}/cl-auto.bashrc ${D}${datadir}/cl-deploy/
	install -m 0644 ${S}/cl-functions.inc ${D}${datadir}/cl-deploy/
	install -m 0644 ${S}/cl-auto.inc ${D}${datadir}/cl-deploy/
	install -m 0644 ${S}/cl-auto.notifier ${D}${datadir}/cl-deploy/

	for dest in pre app;do
        for src_file in $(ls ${S}/*.${dest} 2>/dev/null);do
            dest_file=$(basename ${src_file})
            install -m 0644 ${src_file} ${D}${datadir}/cl-deploy/${dest}/${dest_file}
        done
	done

	touch ${S}/${CL_DEPLOY_BF}
	install -d ${D}/boot
	install -m 0644 ${S}/${CL_DEPLOY_BF} ${D}/boot/
}

do_mtd_copy() {
	[ -f ${S}/cl-deploy.mtd ] && install -m 0755 ${S}/cl-deploy.mtd ${D}${prefix}/local/bin/
}

do_install_append_cm-fx6-evk() {
	do_mtd_copy
}

do_install_append_cl-som-imx6() {
	do_mtd_copy
}

do_install_append_cl-som-imx7() {
	do_mtd_copy
}

do_install_append_cl-som-imx6ul() {
	cp ${S}/cl-deploy.cl-som-imx6ul ${D}${prefix}/local/bin/cl-deploy.platform
}

FILES_${PN} = " \
	${prefix}/local/bin/* \
	${datadir}/* \
	${sysconfdir}/* \
	/boot/* \
"

RDEPENDS_${PN} = "bash pv dialog file gzip bzip2 dosfstools util-linux xz-utils e2fsprogs parted gdisk uuid-runtime bc"
PACKAGE_ARCH = "all"
