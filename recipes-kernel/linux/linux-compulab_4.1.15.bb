require recipes-kernel/linux/linux-imx.inc
require recipes-kernel/linux/linux-dtb.inc

SUMMARY = "CompuLab 4.1.15 kernel"
DESCRIPTION = "Linux kernel for CompuLab imx6(ul) boards."

DEPENDS += "lzop-native bc-native"

SRCBRANCH = "imx_4.1.15_1.0.0_ga"
SRCREV = "77f61547834c4f127b44b13e43c59133a35880dc"
LOCALVERSION = "-cl-1.0"

SRC_URI += "file://0001-platform-add-depends-property-handler.patch"

SRC_URI_append_cm-fx6 += "file://cm-fx6/defconfig \
	file://cm-fx6/0001-ARM-dts-update-support-for-cm-fx6.patch \
	file://cm-fx6/0002-ARM-i.MX6-cm-fx6-add-cm-fx6-platform-driver.patch \
	file://cm-fx6/0003-ARM-i.MX6-cm-fx6-Add-defconfig.patch \
	file://cm-fx6/0004-ARM-i.MX6-cm-fx6-add-gpc-node.patch \
	file://cm-fx6/0005-ARM-i.MX6-audio-add-analog-audio-support.patch \
	file://cm-fx6/0006-ARM-i.MX6-add-depends-property-to-gpmi-nand.patch \
	file://cm-fx6/0007-ARM-i.MX6-update-defconfig-for-nand.patch \
	file://cm-fx6/0008-ARM-i.MX6-add-mxc_dvi-driver.patch \
	file://cm-fx6/0009-ARM-i.MX6-dts-refactoring-of-the-second-video-output.patch \
	file://cm-fx6/0010-ARM-i.MX6-dts-ldo-hdmi-changes.patch \
	file://cm-fx6/0011-ARM-i.MX6-dts-udshc3-set-polarity-value.patch \
	file://cm-fx6/0012-ARM-i.MX6-dts-sata-refactoring.patch \
	file://cm-fx6/0013-ARM-i.MX6-dts-ldb-refactoring.patch \
	file://cm-fx6/0014-ARM-i.MX6-dts-enable-mipi_dsi.patch \
	file://cm-fx6/0015-ARM-i.MX6-pcie-refactoring.patch \
	file://cm-fx6/0016-ARM-i.MX6-dts-clean-up-unnecesary-code.patch \
	file://cm-fx6/0017-ARM-i.MX6-update-defconfig.patch \
	file://cm-fx6/0018-Bluetooth-btmrvl-disable-SD8787-AMP-device.patch \
	file://cm-fx6/0019-ARM-i.MX6-HDMI-Fix-HDMI-PHY-init-hang.patch \
"

SRC_URI_append_cl-som-imx6ul += "file://cl-som-imx6ul/defconfig \
	file://cl-som-imx6ul/0001-ARM-i.MX6UL-dts-Add-initial-support-for-cl-som-imx6u.patch \
	file://cl-som-imx6ul/0002-ARM-i.MX6UL-add-defconfig-for-cl-som-imx6ul.patch \
	file://cl-som-imx6ul/0003-ARM-i.MX6UL-Add-cl-som-imx6ul-platform-driver.patch \
	file://cl-som-imx6ul/0004-Bluetooth-Add-tty-HCI-driver.patch \
	file://cl-som-imx6ul/0005-btwilink-add-minimal-device-tree-support.patch \
	file://cl-som-imx6ul/0006-ARM-i.MX6UL-cl-som-imx6ul-add-support-for-WiLink8.patch \
	file://cl-som-imx6ul/0007-ARM-i.MX6UL-update-cl-som-imx6ul-defconfig-for-wilin.patch \
	file://cl-som-imx6ul/0008-ARM-i.MX6UL-update-defconfig-for-cl-som-imx6ul.patch \
	file://cl-som-imx6ul/0009-sil164-add-dvi-transmitter-simple-driver.patch \
	file://cl-som-imx6ul/0010-ARM-imx6ul-dvi-enable-dvi-output-on-cl-sb-som.patch \
	file://cl-som-imx6ul/0011-ARM-i.MX6UL-update-cl-som-imx6ul-defconfig-for-sil16.patch \
	file://cl-som-imx6ul/0012-ARM-i.MX6UL-update-cl-som-imx6ul-defconfig-for-mcs78.patch \
	file://cl-som-imx6ul/0013-ARM-i.MX6UL-rename-the-dts-files-of-cl-som-imx6ul.patch \
	file://cl-som-imx6ul/0014-ARM-i.MX6UL-update-cl-som-imx6ul-defconfig-for-syste.patch \
	file://cl-som-imx6ul/0015-ARM-i.MX6UL-update-cl-som-imx6ul-defconfig-for-ads78.patch \
	file://cl-som-imx6ul/0016-ARM-i.MX6UL-update-cl-som-imx6ul-defconfig-for-WiFi-.patch \
	file://cl-som-imx6ul/0017-ARM-i.MX6UL-add-nand-support-for-cl-som-imx6ul.patch \
	file://cl-som-imx6ul/0018-ARM-i.MX6UL-enable-cl-som-imx6ul-in-defconfig.patch \
	file://cl-som-imx6ul/0019-ARM-i.MX6UL-dts-fix-the-spi-flash-compatibility-stri.patch \
	file://cl-som-imx6ul/0020-ARM-i.MX6UL-add-depends-property-to-gpmi-nand.patch \
	file://cl-som-imx6ul/0021-ARM-i.MX6UL-dts-set-timing1-as-default-for-lcdif.patch \
	file://cl-som-imx6ul/0022-ARM-i.MX6UL-update-cl-som-imx6ul-defconfig.patch \
	file://cl-som-imx6ul/0023-ARM-i.MX6UL-dts-fix-u-boot-environment-size.patch \
	file://cl-som-imx6ul/0024-ARM-i.MX6UL-dts-update-cl-som-imx6ul-model-string.patch \
	file://cl-som-imx6ul/0025-ARM-i.MX6UL-fix-usdhc1-cd-wp-gpio-settings.patch \
	file://cl-som-imx6ul/0026-ARM-i.MX6UL-move-tsc2046-to-the-ecspi4-bus.patch \
	file://cl-som-imx6ul/0027-ARM-i.MX6UL-enable-flexcan1-for-cl-som-imx6ul.patch \
	file://cl-som-imx6ul/0028-ARM-i.MX6UL-add-aliases-for-cl-som-imx6ul-devices.patch \
	file://cl-som-imx6ul/0029-ARM-i.MX6UL-adjust-usdhc2-pad-conf-values.patch \
	file://cl-som-imx6ul/0030-ARM-i.MX6UL-adjust-usdhc1-pad-conf-values.patch \
	file://cl-som-imx6ul/0031-ASoC-simple-card-Add-system-clock-direction-DT-param.patch \
	file://cl-som-imx6ul/0032-ASoC-simple-card-Add-system-clock-type-DT-parameter-.patch \
	file://cl-som-imx6ul/0033-ARM-i.MX6UL-audio-0-add-analog-audio-support-switch.patch \
	file://cl-som-imx6ul/0034-ARM-i.MX6UL-audio-1-add-analog-audio-support.patch \
	file://cl-som-imx6ul/0035-ARM-i.MX6UL-audio-2-adjust-sai2.MCLK-direction.patch \
	file://cl-som-imx6ul/0036-ARM-i.MX6UL-audio-3-update-defconfig-for-analog-audi.patch \
	file://cl-som-imx6ul/0037-ARM-i.MX6UL-fix-gpmi-depends-string.patch \
	file://cl-som-imx6ul/0038-ARM-i.MX6UL-add-a-missing-cpu0-definition.patch \
"

COMPATIBLE_MACHINE = "(cm-fx6|cl-som-imx6ul)"

inherit fsl-vivante-kernel-driver-handler

IMX_UAPI_HEADERS = "mxc_asrc.h mxc_dcic.h mxcfb.h mxc_mlb.h mxc_sim_interface.h \
                    mxc_v4l2.h ipu.h videodev2.h pxp_device.h pxp_dma.h isl29023.h"

do_install_append () {
   # Install i.MX specific uapi headers
   oe_runmake headers_install INSTALL_HDR_PATH=${B}${exec_prefix}
   install -d ${D}${exec_prefix}/include/linux
   for UAPI_HDR in ${IMX_UAPI_HEADERS}; do
       find ${B}${exec_prefix}/include -name ${UAPI_HDR} -exec cp {} ${D}${exec_prefix}/include/linux \;
       ls ${D}${exec_prefix}/include/linux
       echo "copy ${UAPI_HDR} done"
   done
}

PACKAGES += "linux-imx-soc-headers"
FILES_linux-imx-soc-headers = "${exec_prefix}/include"
