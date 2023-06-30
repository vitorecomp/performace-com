#!/bin/bash

#validate if oc is instalalled
if ! command -v oc &> /dev/null
then
    echo "oc could not be found. Please install oc to continue."
    echo "go install github.com/openshift/oc@latest"
    exit
fi

#validate if the user is looged in
if ! oc whoami &> /dev/null
then
    echo "You are not logged in. Please login to continue."
    echo "oc login"
    exit
fi


gum style \
	 --foreground 69 --border-foreground 156 --border thick \
	--align center --width 50 --margin "1 2" --padding "2 4" \
	'Perfomance Bench' "Installing all the products"

sh ./machine-sets/create-machinesets.sh openshift


sh ./kafka/deploy.sh openshift clean dedicated
sh ./k6/deploy.sh openshift clean dedicated
sh ./prometheus/deploy.sh openshift clean dedicated