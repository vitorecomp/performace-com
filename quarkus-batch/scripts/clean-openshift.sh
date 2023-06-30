#!/bin/bash

# if oc get project performance-bencher &> /dev/null;
# then
#     gum format --theme=pink "### performance-bencher project already exist. Finishing the installation process."
#     oc project performance-bencher
# else
#     oc new-project performance-bencher
# fi

oc delete pod --field-selector=status.phase==Succeeded