FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI =+ "file://fw_env.config"

do_install_append_cm-fx6-evk () {
	install -d ${D}${sysconfdir}/${MACHINE}
	install -m 0644 ${WORKDIR}/fw_env.config ${D}${sysconfdir}/${MACHINE}/fw_env.config
	ln -sf ${MACHINE}/fw_env.config ${D}${sysconfdir}/fw_env.config
}
