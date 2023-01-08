DESCRIPTION = "CompuLab grow rootfs service"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=a0750ac19cd2e5f4cf6dc7876e3dc353"

inherit systemd

RDEPENDS:${PN}:append = " bash parted "

SRC_URI:append = " \
    file://COPYING \
    file://compulab-resize-part.sh \
    file://compulab-grow.service \
"

FILES:${PN}:append = " \
    ${bindir}/compulab-resize-part.sh \
    ${systemd_unitdir}/system/compulab-grow.service \
"

S = "${WORKDIR}"

do_configure() {
	:
}

do_compile() {
	:
}

do_install() {

    install -d -m 755 ${D}${bindir}
    install -m 0755 ${WORKDIR}/compulab-resize-part.sh ${D}/${bindir}/compulab-resize-part.sh

    install -d ${D}/${systemd_unitdir}/system
    install -m 644 ${WORKDIR}/compulab-grow.service ${D}/${systemd_unitdir}/system/
}

pkg_postinst:${PN} () {
    if [ -z $D ]; then
        D="/"
    fi
    systemctl enable compulab-grow.service
}

pkg_prerm:${PN} () {
    if [ -z $D ]; then
        D="/"
    fi
    systemctl disable compulab-grow.service
}
