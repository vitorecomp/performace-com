#!/bin/bash
deploy_k6() {

    deploy_dir=$1
    test_dir=$2

    #get the current dir
    dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

    source $dir/../common/deploy-k6.sh
    
    deploy_k6_operator
    generate_k6_crd

    oc project k6-load


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
}