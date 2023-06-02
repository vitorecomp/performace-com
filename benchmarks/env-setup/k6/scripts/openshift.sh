#!/bin/sh

deploy_k6_operator() {

    output_dir=$1
    cd $output_dir
    
    #validate if the controller-gen is installed
    if
        ! command -v controller-gen &>/dev/null
    then
        echo "controller-gen could not be found. Please install it before running this script"
        echo "run go install github.com/llparse/controller-gen@latest"
        exit
    fi

    #validate if the make is installed
    if
        ! command -v make &>/dev/null
    then
        echo "make could not be found. Please install it before running this script"
        echo "run sudo dnf install make"
        exit
    fi

    # clone the https://github.com/grafana/k6-operator repo if it doesn't exist
    if [ ! -d k6-operator ]; then
        git clone https://github.com/grafana/k6-operator
    fi

    cd k6-operator
    # install the k6-operator
    make deploy
}

run_openshift() {
    dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
    yaml_dir=$1
    output_dir=$2
    clean=$3

    gum format --theme=light "# Deploying to openshift"
    source $dir/../../utils/openshift-utils.sh
    #create the project
    create_project "k6-load" $clean

    gum format --theme=light "## Deploying the k6 operator"
    #deploy the prometheus operator
    deploy_k6_operator $output_dir
}
