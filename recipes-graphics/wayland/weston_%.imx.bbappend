FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://weston.ini-launcher \
    file://weston.ini-plus \
"

do_install:append() {
    WESTON_INI_DEST_DIR=${D}${sysconfdir}/xdg/weston
    cat ${WORKDIR}/weston.ini-launcher >> ${WESTON_INI_DEST_DIR}/weston.ini

    mode=$(awk '(/^\[shell\]/)&&($0="insert")' ${WESTON_INI_DEST_DIR}/weston.ini)
    if [ -z ${mode} ];then
        sed -i '$ a \\n[shell]' ${WESTON_INI_DEST_DIR}/weston.ini
    fi
    sed -i '/^\[shell\]/ a panel-position=bottom' ${WESTON_INI_DEST_DIR}/weston.ini

    cat ${WORKDIR}/weston.ini-plus >> ${WESTON_INI_DEST_DIR}/weston.ini
}

RDEPENDS:${PN} += "cl-launcher"
