FILESEXTRAPATHS_prepend_cl-som-imx6 := "${THISDIR}/compulab:"

SRC_URI_append_cl-som-imx6 = " file://bt-start"

inherit update-alternatives
DEPENDS_append = " update-rc.d-native"

do_install_append_cl-som-imx6 () {
	install -m 0755    ${WORKDIR}/bt-start	${D}${sysconfdir}/init.d
	update-rc.d -r ${D} bt-start defaults
}

MASKED_SCRIPTS += " \
  bt-start \
"
