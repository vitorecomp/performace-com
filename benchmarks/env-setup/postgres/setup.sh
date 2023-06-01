#tranforms lines to an array
generate_operator_yaml() {
    input_dir=$1
    output_dir=$2
    fast=true

    #get the operator options
    if [ "$fast" = true ]; then
        name=crunchy-postgres-operator
        channel=v5
        currentCSV=$(oc get packagemanifests $name -o jsonpath='{.status.channels[?(@.name=="'$channel'")].currentCSV}' | tail -n 1)
    else
        IFS=$'\n'
        name=$(gum choose --selected crunchy-postgres-operator --header "Choose your postgres operator" $(oc get packagemanifests --template='{{range .items}}{{.metadata.name}}{{"\n"}}{{end}}' | grep crunchy-postgres-operator))
        # restore IFS
        unset IFS
        channel=$(gum choose --selected v5 --header "Choose your postgres operator version" $(oc get packagemanifests $name -o jsonpath='{.status.channels[*].name}'))
        currentCSV=$(gum choose --header "Get your initial csv" $(oc get packagemanifests $name -o jsonpath='{.status.channels[?(@.name=="'$channel'")].currentCSV}'))
    fi

    catalog_source=$(oc get packagemanifests $name -o jsonpath='{.status.catalogSource}')
    catalog_source_namespace=$(oc get packagemanifests $name -o jsonpath='{.status.catalogSourceNamespace}')

    export name=$name
    export channel=$channel
    export currentCSV=$currentCSV
    export catalog_source=$catalog_source
    export catalog_source_namespace=$catalog_source_namespace

    envsubst < $input_dir/operator-group-social-database-template.yaml >| $output_dir/operator-group-social-database.yaml
    envsubst < $input_dir/social-database/postgres-operator-template.yaml >| $output_dir/postgres-operator.yaml
}


generate_database_yaml() {
    input_dir=$1
    output_dir=$2
    envsubst < $input_dir/social-database/postgres-crd-template.yaml >| $output_dir/postgres-crd.yaml
}

deploy_database () {

    input_dir=$1

    oc new-project social-database

    gum format --theme=pink "### Applying postgres-operator-group.yaml"
    oc apply -f $input_dir/operator-group-social-database.yaml
    gum format --theme=pink "### Applying postgres-operator.yaml"
    oc apply -f $input_dir/postgres-operator.yaml
    
    
    while [[ $(oc get deployment -n social-database | grep -c pgo) -eq 0 ]]; do
        gum spin --title "wait the postgres deploys" -- sleep 5
    done
    # wait for the operator to be ready
    oc wait --for=condition=available --timeout=600s deployment/pgo -n social-database

    gum format --theme=pink "### Applying postgres-cluster-crd.yaml"
    oc apply -f $input_dir/postgres-crd.yaml

    # # apply all files from deploy_files
    # for file in $dir/../deploy_files/postgres-*; do
    #     #get the file name from a path
    #     file_name=$(basename $file)
    #     gum format --theme=pink "# Applying $file_name"
    #     oc apply -f $file
    # done

}


#todo look if is kubernetes or openshif
#if openshift
#install operator
#install database
#if kubernetes
#install database
