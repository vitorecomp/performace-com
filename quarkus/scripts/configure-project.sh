#!/bin/bash

if oc get project performance-bencher &> /dev/null;
then
    gum format --theme=pink "### performance-bencher project already exist. Finishing the installation process."
    oc project performance-bencher
else
    oc new-project performance-bencher
fi


oc patch namespace performance-bencher  --type='json' -p='[{"op": "add", "path": "/metadata/annotations", "value":{"scheduler.alpha.kubernetes.io/defaultTolerations": "[{\"key\": \"app\", \"operator\": \"Equal\", \"value\": \"application\", \"effect\": \"NoSchedule\"}]", "openshift.io/node-selector": "node/type=application"}}]'
