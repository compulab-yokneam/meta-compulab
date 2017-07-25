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

S = "${WORKDIR}"

do_install() {
	mkdir -p ${D}/usr/local/bin/
	mkdir -p ${D}/usr/share/applications/
	cp ${S}/cl-deploy ${D}/usr/local/bin/
	cp ${S}/cl-deploy.work ${D}/usr/local/bin/
	cp ${S}/cl-deploy.png ${D}/usr/share/applications/
	cp ${S}/cl-deploy.desktop ${D}/usr/share/applications/
}

FILES_${PN} = " \
	/usr/local/bin/* \
	/usr/share/applications/* \
"

ALLOW_EMPTY_${PN} = "1"
RDEPENDS_${PN} = "bash xterm pv dialog"
