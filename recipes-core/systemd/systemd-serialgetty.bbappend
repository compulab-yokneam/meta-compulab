FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://autologin.conf"

do_install:append() {
    if ${@bb.utils.contains("COMPULAB_IMAGE_FEATURES", "serial-autologin-root", "true", "false", d)}; then
        local serial_console="$(printf "${SERIAL_CONSOLES}" | awk -F";" '($0=$2)')"
        install -d ${D}/etc/systemd/system/serial-getty@${serial_console}.service.d/
        install -m 0664 ${UNPACKDIR}/autologin.conf ${D}/etc/systemd/system/serial-getty@${serial_console}.service.d/
    fi
}
