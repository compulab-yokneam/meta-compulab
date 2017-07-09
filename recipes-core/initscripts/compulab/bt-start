#!/bin/bash

### BEGIN INIT INFO
# Provides:		bt-start
# Required-Start:	$syslog
# Required-Stop:	$syslog
# Default-Start:	2 3 4 5
# Default-Stop:		
# Short-Description:	CompuLab bt-start
### END INIT INFO

export BT_REG_ON=204

[[ -d /sys/class/gpio/gpio${BT_REG_ON} ]] && exit 0

bluetoth_start() {
	echo ${BT_REG_ON} > /sys/class/gpio/export
	echo out > /sys/class/gpio/gpio${BT_REG_ON}/direction

	echo 0 > /sys/class/gpio/gpio${BT_REG_ON}/value
	sleep 0.1
	echo 1 > /sys/class/gpio/gpio${BT_REG_ON}/value

	hciattach /dev/ttymxc4 bcm43xx 3000000 flow -t 20 || hciattach /dev/ttymxc4 bcm43xx 3000000 flow -t 20
}

case "$1" in
start)
	bluetoth_start
	;;
stop)
	;;
status)
	;;

*)
	echo "Usage: $0 {start|stop|status}"
	exit 1
esac

exit 0
