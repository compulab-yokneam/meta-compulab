DESCRIPTION = "CompuLab Qt5 Build Environment Image"
LICENSE = "MIT"

inherit populate_sdk_qt5

require dynamic-layers/qt5-layer/recipes-fsl/images/fsl-image-qt5-validation-imx.bb
