DESCRIPTION = "CompuLab grow rootfs service"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=a0750ac19cd2e5f4cf6dc7876e3dc353"

inherit systemd

RDEPENDS:${PN}:append = " bash parted "

SRC_URI:append = " \
    file://COPYING \
    file://compulab-resize-part.sh \
    file://cl-growfs-rootfs.service \
"

FILES:${PN}:append = " \
    ${bindir}/* \
    ${systemd_unitdir}/* \
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
    install -m 644 ${WORKDIR}/${BPN}.service ${D}/${systemd_unitdir}/system/

    install -d ${D}${systemd_unitdir}/system/multi-user.target.wants
    ln -sf ../${BPN}.service ${D}${systemd_unitdir}/system/multi-user.target.wants/${BPN}.service
}

pkg_postinst_ontarget:${PN} () {
    systemctl --system enable cl-growfs-rootfs.service
}

pkg_prerm_ontarget:${PN} () {
    systemctl --system disable cl-growfs-rootfs.service
}
