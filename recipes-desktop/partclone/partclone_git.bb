LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://README.Packages/debian.jessie/copyright;md5=572b984d1a561a020015390325052b53 \
                    file://README.Packages/debian.sid/copyright;md5=572b984d1a561a020015390325052b53 \
                    file://README.Packages/debian.squeeze/copyright;md5=b4de35034cddcf60db39bc7c7061471f \
                    file://README.Packages/debian.wheezy/copyright;md5=572b984d1a561a020015390325052b53 \
                    file://README.Packages/debian/copyright;md5=572b984d1a561a020015390325052b53"

SRC_URI = "git://github.com/Thomas-Tsai/partclone.git;protocol=https;branch=master"

# Modify these as desired
PV = "0.3.37+git"
SRCREV = "ee7762663b28b64ef4921f8d1bafb28050e02dc5"

S = "${UNPACKDIR}/git"

# NOTE: unable to map the following pkg-config dependencies: fuse libntfs-3g
#       (this is based on recipes that have previously been built and packaged)
# NOTE: the following library dependencies are unknown, ignoring: dal ntfs-3g ntfs ufs jfs -luuid nilfs reiserfs aal vmfs -luuid
#       (this is based on recipes that have previously been built and packaged)
DEPENDS = "openssl ncurses e2fsprogs util-linux-libuuid"

# NOTE: if this software is not capable of being built in a separate build directory
# from the source, you should replace autotools with autotools-brokensep in the
# inherit line
inherit pkgconfig gettext autotools

# Specify any options you want to pass to the configure script using EXTRA_OECONF:
EXTRA_OECONF = " --enable-extfs --enable-f2fs --enable-exfat "

FILES:${PN} = " \
	/usr/* \
"
