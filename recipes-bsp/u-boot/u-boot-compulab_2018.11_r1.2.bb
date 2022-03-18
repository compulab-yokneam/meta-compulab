DESCRIPTION = "CompuLab cl-som-imx7 U-Boot"

require recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "https://github.com/compulab/u-boot/archive/refs/tags/v2018.11-cl-som-imx7-hab-1.2.tar.gz"
SRC_URI[md5sum] = "a814fae60402acfd1ebf4e05fc2035d1"
SRC_URI[sha256sum] = "431dfbd210c9d1a6cb348f1c32682b272203e246d8ce025638899fdc6e51680e"

require 2018.11/u-boot-compulab_cl-som-imx7.inc

S = "${WORKDIR}/u-boot-2018.11-cl-som-imx7-hab-1.2"
B = "${WORKDIR}/build"

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE_cl-som-imx7 = "cl-som-imx7"
SECTION = "bootloader"
PROVIDES += "u-boot"
