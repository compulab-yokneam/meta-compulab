SUMMARY = "Performance analysis tools for Linux"
DESCRIPTION = "Performance counters for Linux are a new kernel-based \
subsystem that provide a framework for all things \
performance analysis. It covers hardware level \
(CPU/PMU, Performance Monitoring Unit) features \
and software features (software counters, tracepoints) \
as well."
HOMEPAGE = "https://perf.wiki.kernel.org/index.php/Main_Page"

LICENSE = "GPL-2.0-or-later"

PR = "r1"

PROVIDES = "virtual/perf"

inherit linux-kernel-base kernel-arch kernelsrc

S = "${WORKDIR}/${BP}"

do_compile() {
	:
}

do_install() {
	:
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(cm-fx6-evk|cl-som-imx6ul|cl-som-imx6)"
