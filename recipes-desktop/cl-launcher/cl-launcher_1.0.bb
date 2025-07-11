SUMMARY = "CompuLab Weston Launcher init service"
DESCRIPTION = "CompuLab Weston Launcher init service"
SECTION = "base"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=5b7733d5d8ccd465235c379dbb5e3839"
MAINTAINER = "CompuLab <compulab@compulab.com>"

PACKAGE_ARCH = "${MACHINE_ARCH}"
PR = "r1"

SRC_URI = "file://chromium.png \
	file://chromium \
	file://cl-launcher \
	file://cl-deploy32x32.png \
	file://cl-uboot32x32.png \
	file://cl-camera32x32.png \
	file://terminal.png \
	file://COPYING \
"

S = "${UNPACKDIR}"

ALLOW_EMPTY:${PN} = "1"
RDEPENDS:${PN} = "bash"
FILES:${PN} += "usr/share/compulab/*"

do_install () {

    install -d ${D}/usr/share/compulab/icons/
    install -m 0644 ${UNPACKDIR}/terminal.png ${D}/usr/share/compulab/icons/
    install -m 0644 ${UNPACKDIR}/cl-deploy32x32.png ${D}/usr/share/compulab/icons/
    install -m 0644 ${UNPACKDIR}/cl-uboot32x32.png ${D}/usr/share/compulab/icons/
    install -m 0644 ${UNPACKDIR}/cl-camera32x32.png ${D}/usr/share/compulab/icons/
    install -m 0644 ${UNPACKDIR}/chromium.png ${D}/usr/share/compulab/icons/

    install -d ${D}/usr/share/compulab/scripts/
    install -m 0755 ${UNPACKDIR}/chromium ${D}/usr/share/compulab/scripts/
    install -m 0755 ${UNPACKDIR}/cl-launcher ${D}/usr/share/compulab/scripts/cl-uboot
    install -m 0755 ${UNPACKDIR}/cl-launcher ${D}/usr/share/compulab/scripts/cl-deploy
    install -m 0755 ${UNPACKDIR}/cl-launcher ${D}/usr/share/compulab/scripts/cl-camera

}
