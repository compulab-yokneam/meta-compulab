SUMMARY = "Murata Bluetooth Start Script"
DESCRIPTION = "initscript to set up the Murata Bluetooth."
SECTION = "base"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"
PACKAGE_ARCH = "${MACHINE_ARCH}"
PR = "r1"

INHIBIT_DEFAULT_DEPS = "1"

inherit systemd

SRC_URI = "file://bt-start \
	   file://bt-start.service \
	   file://GPLv2.patch"

SERVICE_NAME = "bt-start.service"
INITSCRIPT_NAME = "bt-start"

S = "${WORKDIR}"

ALLOW_EMPTY:${PN} = "1"
RDEPENDS:${PN} = "bash"
FILES:${PN} += "${systemd_unitdir}/* ${sysconfdir}/* ${sbindir}/*"

do_install () {

    install -d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/${INITSCRIPT_NAME} ${D}${sbindir}

    if ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
            install -d ${D}${sysconfdir}/init.d
            ln -s ${sbindir}/${INITSCRIPT_NAME} ${D}${sysconfdir}/init.d/
    fi

    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -d ${D}${systemd_unitdir}/system
        install -m 644 ${WORKDIR}/${SERVICE_NAME} ${D}/${systemd_unitdir}/system/
    fi

}

pkg_postinst:${PN} () {
	if [ -n "$D" ]; then
		OPTS="--root=$D"
	fi
	systemctl $OPTS enable bt-start.service
}

pkg_postrm:${PN} () {
	if [ -n "$D" ]; then
		OPTS="--root=$D"
	fi
	systemctl $OPTS disable bt-start.service
}
