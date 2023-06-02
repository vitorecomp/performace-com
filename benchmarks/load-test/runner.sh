#!/bin/bash

export USER_SERVICE_URL=http://user-service-social-application.apps.cluster-lwrgl.lwrgl.sandbox2523.opentlc.com
export POST_SERVICE_URL=http://post-service-social-application.apps.cluster-lwrgl.lwrgl.sandbox2523.opentlc.com
export FEED_SERVICE_URL=http://feed-service-social-application.apps.cluster-lwrgl.lwrgl.sandbox2523.opentlc.com

k6 run integration-run.js

generate_k6_crd() {
  dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
  cp $dir/template-deploy/k6-crd-template.yaml $dir/../deploy_files/k6-crd.yaml
}


gum format --theme=pink "### creating the config maps"
oc create configmap social-test --from-file $test_dir/integration-run.js \
    --from-file $test_dir/config-factory.js \
    --from-file $test_dir/user-service-run.js \
    --from-file $test_dir/feed-service-run.js \
    --from-file $test_dir/post-service-run.js

gum format --theme=pink "### creating the sa"
oc create sa k6-runner
oc adm policy add-scc-to-user anyuid -z k6-runner

gum format --theme=pink "## deploying the k6 crds"
oc apply -f $deploy_dir/k6-crd.yaml