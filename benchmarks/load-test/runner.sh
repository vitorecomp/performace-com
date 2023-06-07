#!/bin/bash
###################################### README ######################################

#run example
# -  sh runner.sh openshift kafka-http-burst simple performance-bencher-quarkus-performance-bencher.apps.cluster-tddng.tddng.sandbox999.opentlc.com

###################################### functions ######################################


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
        cd $dir/$application
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

    #delete config map if exist
    if oc get configmap $application-$test_name &> /dev/null;
    then
        oc delete configmap $application-$test_name
    fi
    
    oc create configmap $application-$test_name --from-file $app_dir/integration-run.js --from-file $app_dir/config-factory.js 

    gum format --theme=light "## creating the sa"

    #look if the sa not exist
    if ! oc get sa k6-runner &> /dev/null;
    then
        oc create sa k6-runner
        oc adm policy add-scc-to-user anyuid -z k6-runner
    else
        gum format --theme=light "### sa already exists."
    fi
    

    gum format --theme=light "## deploying the k6 crds"

    #if the crd already exist, delete it
    if oc get k6 $application-$test_name &> /dev/null;
    then
        gum format --theme=light "### k6 crd already exists. Deleting it."
        oc delete k6 $application-$test_name
    fi

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
