#!/bin/sh

deploy_k6_operator() {
  gum format --theme=pink "## Installing the k6 operator"

  #validate if the controller-gen is installed
  if ! command -v controller-gen &> /dev/null
  then
      echo "controller-gen could not be found. Please install it before running this script"
      echo "run go install github.com/llparse/controller-gen@latest"
      exit
  fi

  #validate if the make is installed
  if ! command -v make &> /dev/null
  then
      echo "make could not be found. Please install it before running this script"
      echo "run sudo dnf install make"
      exit
  fi

  #get the current directory
  dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

  # clone the https://github.com/grafana/k6-operator repo if it doesn't exist
  if [ ! -d k6-operator ]; then
    git clone https://github.com/grafana/k6-operator
  fi

  # install the k6-operator
  make deploy
}


generate_k6_crd() {
  dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
  cp $dir/template-deploy/k6-crd-template.yaml $dir/../deploy_files/k6-crd.yaml
}

deploy_k6() {

    deploy_dir=$1
    test_dir=$2
    
    deploy_k6_operator

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