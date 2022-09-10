SRC_URI_append += " \
    https://github.com/compulab-yokneam/logos/raw/master/300x153x8bit/compulab.bmp;protocol=https;md5sum=da59211e201bfcecc53483a0a5c025f1;sha256sum=0a447cc174ca16236ac46c290d6c177cd6be9fc1ad96749c1784d0504522fb47 \
"

do_configure_append () {
    cp -fv ${WORKDIR}/compulab.bmp ${S}/tools/logos/
}

do_install_append_cl-som-imx7 () {
    install -d ${D}/${sysconfdir}
    install -m 0644 ${S}/tools/env/fw_env.${MACHINE}.config  ${D}/${sysconfdir}/fw_env.config
    ln -sf ${UBOOT_INITIAL_ENV}-${MACHINE}-${PV}-${PR} ${D}/${sysconfdir}/u-boot-initial-env
}

FILES_${PN}-env += "/etc/*"
