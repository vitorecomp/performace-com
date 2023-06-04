#!/bin/bash

export USER_SERVICE_URL=http://user-service-social-application.apps.cluster-lwrgl.lwrgl.sandbox2523.opentlc.com

# k6 run integration-run.js

dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
cp $dir/template-deploy/k6-crd-template.yaml $dir/../deploy_files/k6-crd.yaml

gum format --theme=light "### creating the config maps"
oc create configmap social-test --from-file $test_dir/integration-run.js \
    --from-file $test_dir/config-factory.js 

gum format --theme=light "### creating the sa"
oc create sa k6-runner
oc adm policy add-scc-to-user anyuid -z k6-runner

gum format --theme=light "## deploying the k6 crds"
oc apply -f $deploy_dir/k6-crd.yaml