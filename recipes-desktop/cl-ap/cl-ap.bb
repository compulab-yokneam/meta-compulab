# Simple recipe to add desktop icon and executable to run
# CompuLab Access Point

DESCRIPTION = "CompuLab Access Point"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=4a0e2a2916052068a420bbc50873f515"

PR = "r1"

SRC_URI = " \
	file://cl-ap \
	file://cl-ap.work \
	file://cl-ap.desktop \
	file://cl-ap.png \
	file://COPYING \
"

S = "${WORKDIR}"

do_install() {
	mkdir -p ${D}/usr/local/bin/
	mkdir -p ${D}/usr/share/applications/
	cp ${S}/cl-ap ${D}/usr/local/bin/
	cp ${S}/cl-ap.work ${D}/usr/local/bin/
	cp ${S}/cl-ap.png ${D}/usr/share/applications/
	cp ${S}/cl-ap.desktop ${D}/usr/share/applications/
}

FILES_${PN} = " \
	/usr/local/bin/* \
	/usr/share/applications/* \
"

ALLOW_EMPTY_${PN} = "1"
RDEPENDS_${PN} = "bash xterm crda hostapd dhcpcd iptables"
