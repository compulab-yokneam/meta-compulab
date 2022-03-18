DESCRIPTION = "CompuLab cl-som-imx7 U-Boot"

require recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "https://github.com/compulab/u-boot/archive/refs/tags/v2017.07-cl-som-imx7-1.8.tar.gz"
SRC_URI[md5sum] = "ead2b259efe5d4e7dbd3cab07d3c947c"
SRC_URI[sha256sum] = "889ee434f84205d75a35ae7edb100b1a01e967049e4bcaf7e5b84b43b9cd6e9c"

require 2017.07/u-boot-compulab_cl-som-imx7.inc

S = "${WORKDIR}/u-boot-2017.07-cl-som-imx7-1.8"
B = "${WORKDIR}/build"

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE_cl-som-imx7 = "cl-som-imx7"
SECTION = "bootloader"
PROVIDES += "u-boot"
