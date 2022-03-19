FILESEXTRAPATHS_prepend := "${THISDIR}/cl-som-imx7:"

SRC_URI_append_cl-som-imx7 += " \
	file://0001-cl-som-imx7_defconfig.patch  \
	file://0002-Makefile-2018.11.patch \
"
