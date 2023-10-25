FILESEXTRAPATHS:prepend := "${THISDIR}/cl-som-imx7:"

SRC_URI:append:cl-som-imx7 = " \
	file://0001-cl-som-imx7_defconfig.patch  \
"
