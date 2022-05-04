LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://README;md5=2456088a0455a82ac9e16b007de97c03"
DEPENDS = "u-boot-mkimage-native"

S = "${WORKDIR}"

inherit deploy

SRC_URI = "file://README \
    file://bootscr \
"

IBOOTSCRIPT ?= "bootscr"
OBOOTSCRIPT ?= "boot.scr"

do_compile () {
	# allow deploy to use the ${MACHINE} name to simplify things
	if [ ! -d ${S}/board/compulab/${MACHINE} ]; then
		mkdir -p ${S}/board/compulab/${MACHINE}
	fi

	uboot-mkimage -A arm -O linux -T script -C none -a 0 -e 0 \
		-n "boot script" -d ${S}/${IBOOTSCRIPT} \
		${S}/board/compulab/${MACHINE}/${OBOOTSCRIPT}
}

do_install () {
    install -d ${D}/boot
    install ${S}/board/compulab/${MACHINE}/${OBOOTSCRIPT} \
            ${D}/boot/${OBOOTSCRIPT}
}

do_deploy () {
    install -d ${DEPLOYDIR}
    install ${S}/board/compulab/${MACHINE}/${OBOOTSCRIPT} \
            ${DEPLOYDIR}/${OBOOTSCRIPT}-${MACHINE}-${PV}-${PR}

    cd ${DEPLOYDIR}
    ln -sf ${OBOOTSCRIPT}-${MACHINE}-${PV}-${PR} ${OBOOTSCRIPT}-${MACHINE}
    ln -sf ${OBOOTSCRIPT}-${MACHINE}-${PV}-${PR} ${OBOOTSCRIPT}
}

addtask deploy after do_install before do_build

do_populate_sysroot[noexec] = "1"

FILES:${PN} = "/boot"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(cm-fx6-evk|cl-som-imx6|cl-som-imx6ul)"
