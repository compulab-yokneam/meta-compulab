#!/bin/bash -e

mode=$( [[ -f /etc/debian_version ]]  && printf "debian" || printf "yocto" )
mode=debian
source /usr/local/bin/cl-deploy.${mode}.mod
