# Simple recipe to add desktop icon and executable to run
# CompuLab Deployment Tool

DESCRIPTION = "CompuLab Deployment Tool"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=4a0e2a2916052068a420bbc50873f515"

PR = "r1"

SRC_URI = " \
	file://cl-deploy \
	file://cl-deploy.work \
	file://cl-deploy.desktop \
	file://cl-deploy.png \
	file://COPYING \
"

SRC_URI_append_cl-som-imx6ul = " \
	file://cl-deploy.cl-som-imx6ul \
"

S = "${WORKDIR}"

do_install() {
	mkdir -p ${D}/usr/local/bin/
	mkdir -p ${D}/usr/share/applications/
	cp ${S}/cl-deploy ${D}/usr/local/bin/
	cp ${S}/cl-deploy.work ${D}/usr/local/bin/
	cp ${S}/cl-deploy.png ${D}/usr/share/applications/
	cp ${S}/cl-deploy.desktop ${D}/usr/share/applications/
}

do_install_append_cl-som-imx6ul() {
	cp ${S}/cl-deploy.cl-som-imx6ul ${D}/usr/local/bin/cl-deploy.platform
}

FILES_${PN} = " \
	/usr/local/bin/* \
	/usr/share/applications/* \
"

ALLOW_EMPTY_${PN} = "1"
RDEPENDS_${PN} = "bash xterm pv dialog u-boot-fw-utils"
