# Simple recipe to add desktop icon and executable to run
# CompuLab U-Boot Tool

DESCRIPTION = "CompuLab U-Boot Tool"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=258fbdf7b6336b41e0980f2046ff2ac0"
MAINTAINER = "CompuLab <compulab@compulab.com>"

PR = "r1"

SRC_URI = " \
	file://cl-uboot \
	file://cl-uboot.work \
	file://cl-uboot.quiet \
	file://cl-uboot.desktop \
	file://cl-uboot.png \
	file://COPYING \
"

S = "${WORKDIR}"

do_install() {
	mkdir -p ${D}/usr/local/bin/
	mkdir -p ${D}/usr/share/applications/
	cp ${S}/cl-uboot ${D}/usr/local/bin/
	cp ${S}/cl-uboot.work ${D}/usr/local/bin/
	cp ${S}/cl-uboot.quiet ${D}/usr/local/bin/
	cp ${S}/cl-uboot.png ${D}/usr/share/applications/
	cp ${S}/cl-uboot.desktop ${D}/usr/share/applications/
}

FILES:${PN} = " \
	/usr/local/bin/* \
	/usr/share/applications/* \
"

RDEPENDS:${PN} = "bash pv dialog mtd-utils u-boot-compulab"
PACKAGE_ARCH = "all"
