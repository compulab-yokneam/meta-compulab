SUMMARY = "Murata Bluetooth Start Script"
DESCRIPTION = "initscript to set up the Murata Bluetooth."
SECTION = "base"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"
PACKAGE_ARCH = "${MACHINE_ARCH}"
PR = "r1"

INHIBIT_DEFAULT_DEPS = "1"

# As the recipe doesn't inherit systemd.bbclass, we need to set this variable
# manually to avoid unnecessary postinst/preinst generated.
python __anonymous() {
    if not bb.utils.contains('DISTRO_FEATURES', 'sysvinit', True, False, d):
        d.setVar("INHIBIT_UPDATERCD_BBCLASS", "1")
}

inherit update-rc.d

SRC_URI = "file://bt-start.sh \
	   file://GPLv2.patch"

INITSCRIPT_NAME = "bt-start.sh"
INITSCRIPT_PARAMS = "start 2 3 4 5"

S = "${WORKDIR}"

do_install () {
    # Only install the script if 'sysvinit' is in DISTRO_FEATURES
    # THe ulitity this script provides could be achieved by systemd-vconsole-setup.service
    if ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/${INITSCRIPT_NAME} ${D}${sysconfdir}/init.d/
	update-rc.d -r ${D} ${INITSCRIPT_NAME} defaults
    fi
}

DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES','systemd','systemd-systemctl-native','',d)}"
pkg_postinst_${PN} () {
	if ${@bb.utils.contains('DISTRO_FEATURES','systemd sysvinit','true','false',d)}; then
		if [ -n "$D" ]; then
			OPTS="--root=$D"
		fi
		systemctl $OPTS mask bt-start.service
	fi
}

ALLOW_EMPTY_${PN} = "1"
RDEPENDS_${PN} = "bash"
