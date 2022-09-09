FILESEXTRAPATHS_prepend := "${THISDIR}/cl-som-imx7:"

SRC_URI_append_cl-som-imx7 += " \
	file://HACK-fdt-avoid-building-the-new-python-libfdt-module.patch \
"
