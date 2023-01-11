DESCRIPTION = "CompuLab ssh-keygen"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=fea5d6bf0b379b6923870fba9d0d3ac2"

inherit systemd

RDEPENDS:${PN}:append = " bash "

SRC_URI:append = " \
    file://COPYING \
    file://compulab-ssh-init.sh \
    file://compulab-ssh-purge.sh \
    file://compulab-ssh-init.service \
"

FILES:${PN}:append = " \
    ${bindir}/compulab-ssh-init.sh \
    ${bindir}/compulab-ssh-purge.sh \
    ${systemd_unitdir}/system/compulab-ssh-init.service \
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
    install -m 0755 ${WORKDIR}/compulab-ssh-init.sh ${D}/${bindir}/compulab-ssh-init.sh
    install -m 0755 ${WORKDIR}/compulab-ssh-purge.sh ${D}/${bindir}/compulab-ssh-purge.sh

    install -d ${D}/${systemd_unitdir}/system
    install -m 644 ${WORKDIR}/compulab-ssh-init.service ${D}/${systemd_unitdir}/system/
}

pkg_postinst:${PN} () {
    if [ -z $D ]; then
        D="/"
    fi
    systemctl enable compulab-ssh-init.service
    rootfs=$D $D/usr/bin/compulab-ssh-purge.sh
}

pkg_prerm:${PN} () {
    systemctl disable compulab-ssh-init.service
}
