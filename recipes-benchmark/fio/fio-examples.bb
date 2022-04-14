DESCRIPTION = "Flexible I/O tester examples"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=39eac9f8f195f04c16966a9f5824e9ba"

SRC_URI = "git://github.com/compulab-yokneam/fio-examples.git;protocol=https"
SRCREV = "master"

PR = "r0"

S = "${WORKDIR}/git"

FS_LOCATION = "/unit_tests/fio-examples"

do_install () {
    install -d ${D}${FS_LOCATION}
    for src_file in $(ls ${S}/*.fio 2>/dev/null);do
        dest_file=$(basename ${src_file})
        install -m 0644 ${src_file} ${D}${FS_LOCATION}/${dest_file}
    done
}

FILES_${PN} = " \
	${FS_LOCATION}/* \
"

PROVIDES += "${PN}"
PACKAGE_ARCH = "${MACHINE_ARCH}"
