CL_FUNCTIONS=/usr/share/cl-deploy/cl-functions.inc
if [[ ! -f ${CL_FUNCTIONS} ]];then
cat << eof
The package library file ${CL_FUNCTIONS} is not found.
Reinstall the cl-deploy package.
eof
exit 3
fi

source ${CL_FUNCTIONS}

conf=/etc/cl-auto.conf
sample=/usr/share/cl-deploy/cl-auto.conf.sample

show_conf() {
[[ -f ${conf} ]] && cat ${conf} || cat <<< "${conf} not found"
}

get_mode() {
[[ $(basename $(readlink /sbin/init)) == 'cl-init' ]] && echo "on" || echo "off"
}

get_auto_device() {
dev_name=$(get_root_device)
echo -n ${dev_name}
}

toggle_dr() {
    [[ ${dry_run:-"no"} = "no" ]] && export dry_run="yes" || export dry_run="no"
}

toggle_dr_res() {
    [[ ${dry_run_res:-0} -eq 0  ]] && dry_run_res=1 || dry_run_res=0
}

get_dr() {
    [[ ${dry_run} = "yes" ]]  && echo -n ${dry_run}"=${dry_run_res} " || echo -n ${dry_run}
}

usage () {
cat << eof
  on/E -- Enable auto install boot mode
 off/D -- Disable auto install boot mode
     M -- Modify ${conf} file
     C -- Copy ${sample} file to /etc/cl-auto.conf
     G -- Start autoinstaller now
     U -- Toggle dry run
     X -- Exit cl-auto.shell
     B -- Fast reboot
     O -- Poweroff


Conf file:
$(show_conf)

eof
}

PS1='$(usage)\n\nautoinstaller ( device: $(get_auto_device) ;  boot mode : $(get_mode) ; dry run : $(get_dr)) > '
set -m

alias G='cl-auto'
alias E='cl-auto -A'
alias on='cl-auto -A'
alias D='cl-auto -a'
alias off='cl-auto -a'
alias C='cl-auto -c'
alias M='vi ${conf}'
alias X='exit'
alias B='cl-reboot'
alias O='cl-poff'
alias U='toggle_dr'
alias UU='toggle_dr_res'
