DESCRIPTION = "A XFCE desktop demo image."

IMAGE_FEATURES += "splash package-management ssh-server-dropbear hwcodecs dev-pkgs nfs-client"

LICENSE = "MIT"

inherit core-image distro_features_check

REQUIRED_DISTRO_FEATURES = "x11"

CORE_IMAGE_BASE_INSTALL += "\
	packagegroup-core-boot \
	packagegroup-core-x11 \
	packagegroup-xfce-base \
	nodejs \
	packagegroup-fsl-gstreamer1.0-full \
	gstreamer1.0-plugins-imx \
	libtool \
	libexif \
	python-compiler \
	u-boot-fw-utils \
	nfs-utils \
	nfs-utils-client \
	cl-deploy \
	video-input \
	can-utils \
"

CORE_IMAGE_BASE_INSTALL_append_cl-som-imx6 += "chromium"
