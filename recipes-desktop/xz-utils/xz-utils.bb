LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

SRC_URI = "file://README"

S = "${WORKDIR}"

do_configure () {
	# Specify any needed configure commands here
	:
}

do_compile () {
	# Specify compilation commands here
	:
}

do_install () {
	install -d ${D}/opt/compulab/${PN}
	install -m 0644 ${S}/README ${D}/opt/compulab/${PN}
}

FILES_${PN} = " \
	/opt \
"

RDEPENDS_${PN} += "xz"
