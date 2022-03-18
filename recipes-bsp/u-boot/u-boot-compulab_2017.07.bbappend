FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

B = "${WORKDIR}/build"

SRC_URI_append_cl-som-imx7 +=  "\
    file://0999-cl-som-imx7_gg.patch \
"
