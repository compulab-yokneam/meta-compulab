DESCRIPTION = "CompuLab cl-som-imx7 U-Boot"

require recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/compulab/u-boot.git;branch=cl-som-imx7/dev;protocol=https"
SRCREV = "f697db12baf28f133b6d9d606de629e9eb58b7dc"

PR = "r1.9"

require u-boot-compulab-2017.07/u-boot-compulab_cl-som-imx7.inc

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "cl-som-imx7"
SECTION = "bootloader"
PROVIDES += "u-boot"
