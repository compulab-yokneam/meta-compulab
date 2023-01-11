#!/bin/bash

set -e

dpkg -L openssh-server &>/dev/null || exit 0

[[ ! -f /etc/ssh/sshd_config ]] || exit 0

dpkg-reconfigure openssh-server
