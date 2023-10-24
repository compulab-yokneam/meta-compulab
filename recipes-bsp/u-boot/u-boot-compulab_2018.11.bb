DESCRIPTION = "CompuLab cl-som-imx7 U-Boot"

require recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/compulab/u-boot.git;branch=cl-som-imx7/hab;protocol=https"
SRCREV = "0d5ef5d5c387733d37263ce43589921b07b9a2a9"

PR = "r1.2.1"

require u-boot-compulab-2018.11/u-boot-compulab_cl-som-imx7.inc

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE_cl-som-imx7 = "cl-som-imx7"
SECTION = "bootloader"
PROVIDES += "u-boot"
