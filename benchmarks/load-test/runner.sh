#!/bin/bash
run_openshift(){
    gum format --theme=light "# Deploy test $test_name of $application on Openshit"

    application=$1
    test_name=$2
    app_url=$3
    base_dir=$4


    if [ -z "$application" ]; then
        application=$(gum choose "kafka-http-burst" "slow-dependecy")
    fi


    mkdir -p $base_dir/tmp

    yml_dir=$base_dir/yaml
    tmp_dir=$base_dir/tmp
    app_dir=$base_dir/$application

    if [ "$application" == "kafka-http-burst" ]; then
        cd $dir/kafka-http-burst
        k6 run integration-run.js
    elif [ "$application" == "slow-dependecy" ]; then
        echo "slow-dependecy was no implemented it."
        echo "fell free to contribute, any help is welcome."
        exit
    else
        echo "Invalid option"
        exit
    fi


    export application=$application
    export test_name=$test_name
    export app_url=$app_url

    envsubst < $yml_dir/runner-template.yaml >| $tmp_dir/runner.yaml

    gum format --theme=light "## creating the config maps"
    
    oc create configmap $application-$test_name --from-file $app_dir/integration-run.js --from-file $app_dir/config-factory.js 

    gum format --theme=light "## creating the sa"
    oc create sa k6-runner
    oc adm policy add-scc-to-user anyuid -z k6-runner

    gum format --theme=light "## deploying the k6 crds"
    oc apply -f $tmp_dir/runner.yaml

}


###################################### main ######################################
#getting the script dir
dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

# test if gum is installed
if ! command -v gum &> /dev/null
then
    echo "gum could not be found. Please install gum to continue."
    echo "go install github.com/charmbracelet/gum@latest"
    exit
fi

cluster=$1
application=$2
test_name=$3
app_url=$4

gum style \
	 --foreground 69 --border-foreground 156 --border thick \
	--align center --width 50 --margin "1 2" --padding "2 4" \
	'Perfomance Bench' "Running a benckmark on cluster!"

#test if the environment name is provided
if [ -z "$cluster" ]; then
    cluster=$(gum choose "kubernetes" "openshift")
fi


if [ "$cluster" == "kubernetes" ]; then
    echo "Kubernetes was no implemented it."
    echo "fell free to contribute, any help is welcome."
    exit
else
    run_openshift $application $test_name $app_url $dir
fi






#test if the environment name is provided
