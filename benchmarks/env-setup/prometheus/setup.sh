#!/bin/bash

dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

generate_operator_group_yaml() {
    template_dir=$1
    output_dir=$2

    envsubst < $template_dir/operator-group-monitoring-template.yaml >| $output_dir/operator-group-monitoring.yaml
}

apply_monitoring_group() {
    output_dir=$1

    oc apply -f $output_dir/operator-group-monitoring.yaml
}

generate_prometheus_operator_yaml() {
    template_dir=$1
    output_dir=$2
    fast=true

    #get the operator options
    if [ "$fast" = true ]; then
        name=prometheus
        catalog_source=community-operators
        unset IFS
        channel=$(oc get packagemanifest -o json | jq '.items[] | select(.metadata.labels.catalog =="'$catalog_source'") | select(.metadata.name=="'$name'")' | jq .status.defaultChannel)
        #remove quota from vars
        channel=$(echo $channel | sed 's/"//g')
        currentCSV=$(oc get packagemanifest -o json | jq '.items[] | select(.metadata.labels.catalog =="'$catalog_source'") | select(.metadata.name=="'$name'")' | jq '.status.channels[] | select(.name=="'$channel'")' | jq .currentCSV)
        #remove quota from vars
        currentCSV=$(echo $currentCSV | sed 's/"//g')
    else
        IFS=$'\n'
        name=$(gum choose --selected prometheus --header "Choose your prometheus operator" $(oc get packagemanifests --template='{{range .items}}{{.metadata.name}}{{"\n"}}{{end}}' | grep prometheus))
        # restore IFS
        unset IFS
        channel=$(gum choose --selected beta --header "Choose your prometheus operator version" $(oc get packagemanifests $name -o jsonpath='{.status.channels[*].name}'))
        currentCSV=$(gum choose --header "Get your initial csv" $(oc get packagemanifests $name -o jsonpath='{.status.channels[?(@.name=="'$channel'")].currentCSV}'))
        catalog_source=$(oc get packagemanifests $name -o jsonpath='{.status.catalogSource}')
    fi


    catalog_source_namespace=$(oc get packagemanifests $name -o jsonpath='{.status.catalogSourceNamespace}')

    export name=$name
    export channel=$channel
    export currentCSV=$currentCSV
    export catalog_source=$catalog_source
    export catalog_source_namespace=$catalog_source_namespace

    
    envsubst < $template_dir/monitoring/operator-prometheus-template.yaml >| $output_dir/operator-prometheus.yaml
}


generate_prometheus_crd_yaml() {
    template_dir=$1

    envsubst < $template_dir/monitoring/crd-prometheus-template.yaml >| $output_dir/crd-prometheus.yaml
}

apply_prometheus() {
    deploy_dir=$1

    oc apply -f $deploy_dir/operator-prometheus.yaml

    gum spin --title "wait prometheus subscription" -- oc wait --for=condition=AtLatestKnown --timeout=600s subscription/prometheus -n monitoring
    gum spin --title "wait prometheus operator" -- oc wait --for=condition=available --timeout=600s deployment/prometheus-operator -n monitoring

    oc apply -f $deploy_dir/crd-prometheus.yaml
}

generate_grafana_operator_yaml(){
    input_dir=$1
    output_dir=$2
    fast=true

    #get the operator options
    if [ "$fast" = true ]; then
        name=grafana-operator
        catalog_source=community-operators
        unset IFS
        channel=$(oc get packagemanifest -o json | jq '.items[] | select(.metadata.labels.catalog =="'$catalog_source'") | select(.metadata.name=="'$name'")' | jq .status.defaultChannel)
        #remove quota from vars
        channel=$(echo $channel | sed 's/"//g')
        currentCSV=$(oc get packagemanifest -o json | jq '.items[] | select(.metadata.labels.catalog =="'$catalog_source'") | select(.metadata.name=="'$name'")' | jq '.status.channels[] | select(.name=="'$channel'")' | jq .currentCSV)
        #remove quota from vars
        currentCSV=$(echo $currentCSV | sed 's/"//g')
        
    else
        IFS=$'\n'
        name=$(gum choose --selected grafana --header "Choose your grafana operator" $(oc get packagemanifests --template='{{range .items}}{{.metadata.name}}{{"\n"}}{{end}}' | grep grafana))
        # restore IFS
        unset IFS
        channel=$(gum choose --selected v4 --header "Choose your grafana operator version" $(oc get packagemanifests $name -o jsonpath='{.status.channels[*].name}'))
        currentCSV=$(gum choose --selected grafana-operator.v4.10.0 --header "Get your initial csv" $(oc get packagemanifests $name -o jsonpath='{.status.channels[?(@.name=="'$channel'")].currentCSV}'))
        catalog_source=$(oc get packagemanifests $name -o jsonpath='{.status.catalogSource}')
    fi
    catalog_source_namespace=$(oc get packagemanifests $name -o jsonpath='{.status.catalogSourceNamespace}')

    export name=$name
    export channel=$channel
    export currentCSV=$currentCSV
    export catalog_source=$catalog_source
    export catalog_source_namespace=$catalog_source_namespace

    envsubst < $input_dir/monitoring/operator-grafana-template.yaml >| $output_dir/operator-grafana.yaml
}

generate_grafana_crd_yaml() {
    envsubst < $input_dir/monitoring/crd-grafana-template.yaml >| $output_dir/crd-grafana.yaml
    envsubst < $input_dir/monitoring/crd-grafana-datasource-template.yaml >| $output_dir/crd-grafana-datasource.yaml
    cat $input_dir/monitoring/crd-grafana-dashboard-template.yaml >| $output_dir/crd-grafana-dashboard.yaml
}

apply_grafana() {
    oc apply -f $dir/../deploy_files/operator-grafana.yaml

    gum spin --title "wait grafana subscription" -- oc wait --for=condition=AtLatestKnown --timeout=600s subscription/grafana -n monitoring
    gum spin --title "wait grafana operator" -- oc wait --for=condition=available --timeout=600s deployment/grafana-operator-controller-manager -n monitoring

    # deploy the grafana crd for the dashboard and datasource
    oc apply -f $dir/../deploy_files/crd-grafana-datasource.yaml
    oc apply -f $dir/../deploy_files/crd-grafana-dashboard.yaml

    # deploy the base crd 
    oc apply -f $dir/../deploy_files/crd-grafana.yaml

    gum spin --title "wait grafana deploy" -- oc wait --for=condition=available --timeout=600s deployment/grafana-deployment -n monitoring

    #wait for the grafana service to exist
    while [ -z "$(oc get svc -n monitoring | grep grafana-service)" ]; do
        gum spin --title "wait grafana service" -- sleep 5
    done
    #wait for the grafana service to be available
    oc expose svc/grafana-service -n monitoring

}

#create the monitoring project
deploy_monitoring() {
    template_dir=$1
    output_dir=$2

    generate_operator_group_yaml $template_dir $output_dir
    apply_monitoring_group $output_dir

    #install the prometheus
    generate_prometheus_operator_yaml $template_dir $output_dir
    generate_prometheus_crd_yaml $template_dir $output_dir
    apply_prometheus $output_dir

    # #install the grafana
    generate_grafana_operator_yaml $template_dir $output_dir
    generate_grafana_crd_yaml $template_dir $output_dir
    apply_grafana $output_dir
    
}

