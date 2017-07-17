FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = " \
    file://pseudo-glibc-rtld-next-workaround.patch \
"
