FILESEXTRAPATHS:prepend := "${THISDIR}/cl-som-imx7:"

SRC_URI:append:cl-som-imx7 += " \
	file://0001-Makefile-2017.07.patch \
	file://HACK-fdt-avoid-building-the-new-python-libfdt-module.patch \
"
