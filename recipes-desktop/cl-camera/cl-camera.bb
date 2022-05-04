# Video Input Test recipe to add executable to run gstreamer
# command to display video input on the default videosink.

DESCRIPTION = "Video Input Test"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=3af51c5f8b7ed40f288f000169a1595d"

PR = "r1"

SRC_URI = " \
	file://cl-camera \
	file://cl-camera.desktop \
	file://cl-camera.png \
	file://COPYING \
"

S = "${WORKDIR}"

do_install() {
	install -d ${D}/usr/local/bin/
	install -d ${D}/usr/share/applications/
	install -m 0755 ${S}/cl-camera ${D}/usr/local/bin/
	install -m 0644 ${S}/cl-camera.png ${D}/usr/share/applications/
	install -m 0644 ${S}/cl-camera.desktop ${D}/usr/share/applications/
}

FILES:${PN} = " \
	/usr \
"

ALLOW_EMPTY:${PN} = "1"
RDEPENDS:${PN} = "bash gstreamer1.0 gstreamer1.0-plugins-good"
