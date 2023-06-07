generate_operator_group_yaml() {
    template_dir=$1
    output_dir=$2

    envsubst <$template_dir/operator-group-monitoring-template.yaml >|$output_dir/operator-group-monitoring.yaml
}

apply_operator_group() {
    output_dir=$1

    oc apply -f $output_dir/operator-group-monitoring.yaml
}

generate_prometheus_operator_yaml() {
    template_dir=$1
    output_dir=$2

    #get the operator options

    name=prometheus
    catalog_source=community-operators
    channel=$(oc get packagemanifest -o json | jq '.items[] | select(.metadata.labels.catalog =="'$catalog_source'") | select(.metadata.name=="'$name'")' | jq .status.defaultChannel)
    channel=$(echo $channel | sed 's/"//g')
    currentCSV=$(oc get packagemanifest -o json | jq '.items[] | select(.metadata.labels.catalog =="'$catalog_source'") | select(.metadata.name=="'$name'")' | jq '.status.channels[] | select(.name=="'$channel'")' | jq .currentCSV)
    currentCSV=$(echo $currentCSV | sed 's/"//g')
    catalog_source_namespace=$(oc get packagemanifests $name -o jsonpath='{.status.catalogSourceNamespace}')

    export name=$name
    export channel=$channel
    export currentCSV=$currentCSV
    export catalog_source=$catalog_source
    export catalog_source_namespace=$catalog_source_namespace

    envsubst <$template_dir/operator-prometheus-template.yaml >|$output_dir/operator-prometheus.yaml
}

generate_prometheus_crd_yaml() {
    template_dir=$1
    output_dir=$2

    envsubst <$template_dir/crd-prometheus-template.yaml >|$output_dir/crd-prometheus.yaml
}

apply_prometheus() {
    deploy_dir=$1

    oc apply -f $deploy_dir/operator-prometheus.yaml

    gum spin --title "wait prometheus subscription" -- oc wait --for=condition=AtLatestKnown --timeout=600s subscription/prometheus -n monitoring
    gum spin --title "wait prometheus operator" -- oc wait --for=condition=available --timeout=600s deployment/prometheus-operator -n monitoring

    oc apply -f $deploy_dir/crd-prometheus.yaml
}

generate_grafana_operator_yaml() {
    input_dir=$1
    output_dir=$2
    fast=true

    #get the operator options
    name=grafana-operator
    catalog_source=community-operators
    unset IFS
    channel=$(oc get packagemanifest -o json | jq '.items[] | select(.metadata.labels.catalog =="'$catalog_source'") | select(.metadata.name=="'$name'")' | jq .status.defaultChannel)
    channel=$(echo $channel | sed 's/"//g')
    currentCSV=$(oc get packagemanifest -o json | jq '.items[] | select(.metadata.labels.catalog =="'$catalog_source'") | select(.metadata.name=="'$name'")' | jq '.status.channels[] | select(.name=="'$channel'")' | jq .currentCSV)
    currentCSV=$(echo $currentCSV | sed 's/"//g')
    catalog_source_namespace=$(oc get packagemanifests $name -o jsonpath='{.status.catalogSourceNamespace}')

    export name=$name
    export channel=$channel
    export currentCSV=$currentCSV
    export catalog_source=$catalog_source
    export catalog_source_namespace=$catalog_source_namespace

    envsubst <$input_dir/operator-grafana-template.yaml >|$output_dir/operator-grafana.yaml
}

apply_grafana_operator() {
    deploy_dir=$1

    oc apply -f $deploy_dir/operator-grafana.yaml

    gum spin --title "wait grafana subscription" -- oc wait --for=condition=AtLatestKnown --timeout=600s subscription/grafana -n monitoring
    gum spin --title "wait grafana operator" -- oc wait --for=condition=available --timeout=600s deployment/grafana-operator-controller-manager -n monitoring

}

generate_grafana_datasource_yaml() {
    template_dir=$1
    output_dir=$2

    cat $input_dir/crd-grafana-datasource-template.yaml >|$output_dir/crd-grafana-datasource.yaml
    cat $input_dir/crd-grafana-datasource-openshift-prometheus-template.yaml >|$output_dir/crd-grafana-datasource-openshift-prometheus.yaml
}

apply_grafana_datasource() {
    deploy_dir=$1


    oc project openshift-monitoring
    oc get secret prometheus-k8s-htpasswd -o jsonpath='{.data.auth}' | base64 -d > /tmp/htpasswd-tmp
    echo >> /tmp/htpasswd-tmp #<-- MUST HAVE NEWLINE ADDED
    htpasswd -s -b  /tmp/htpasswd-tmp grafana-test topsecret
    oc patch secret prometheus-k8s-htpasswd -p "{\"data\":{\"auth\":\"$(base64 -w0 /tmp/htpasswd-tmp)\"}}"
    oc delete pod -l app=prometheus
    oc project monitoring

    oc apply -f $deploy_dir/crd-grafana-datasource.yaml
    oc apply -f $deploy_dir/crd-grafana-datasource-openshift-prometheus.yaml
}

generate_grafana_dashboard_yaml() {
    template_dir=$1
    output_dir=$2

    cat $input_dir/crd-grafana-dashboard-template.yaml >|$output_dir/crd-grafana-dashboard.yaml
}

apply_grafana_dashboard() {
    deploy_dir=$1

    oc apply -f $deploy_dir/crd-grafana-dashboard.yaml

}

generate_grafana_crd_yaml() {
    cat $input_dir/crd-grafana-template.yaml >|$output_dir/crd-grafana.yaml
}

apply_grafana_crd() {
    deploy_dir=$1

    oc apply -f $deploy_dir/crd-grafana.yaml

    gum spin --title "wait grafana deploy" -- oc wait --for=condition=available --timeout=600s deployment/grafana-deployment -n monitoring

    #wait for the grafana service to exist
    while [ -z "$(oc get svc -n monitoring | grep grafana-service)" ]; do
        gum spin --title "wait grafana service" -- sleep 5
    done
    #wait for the grafana service to be available
    oc expose svc/grafana-service -n monitoring
}

deploy_operator_group() {
    template_dir=$1
    output_dir=$2

    generate_operator_group_yaml $template_dir $output_dir
    apply_operator_group $output_dir
}

deploy_prometheus_operator() {
    template_dir=$1
    output_dir=$2

    generate_prometheus_operator_yaml $template_dir $output_dir
    generate_prometheus_crd_yaml $template_dir $output_dir
    apply_prometheus $output_dir
}

deploy_grafana_operator() {
    template_dir=$1
    output_dir=$2

    generate_grafana_operator_yaml $template_dir $output_dir
    generate_grafana_crd_yaml $template_dir $output_dir
    apply_grafana_operator $output_dir
}

deploy_grafana_datasource() {
    template_dir=$1
    output_dir=$2

    generate_grafana_datasource_yaml $template_dir $output_dir
    apply_grafana_datasource $output_dir
}

deploy_grafana_dashboard() {
    template_dir=$1
    output_dir=$2

    generate_grafana_dashboard_yaml $template_dir $output_dir
    apply_grafana_dashboard $output_dir
}

deploy_grafana() {
    template_dir=$1
    output_dir=$2

    generate_grafana_crd_yaml $template_dir $output_dir
    apply_grafana_crd $output_dir
}

run_openshift() {
    template_dir=$1
    output_dir=$2
    clean=$3

    dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"
    source $dir/../../utils/openshift-utils.sh

    gum format --theme=light "# Deploying to openshift"
    gum format --theme=light "## Creating the project"

    create_project "monitoring" $clean

    gum format --theme=light "## Creating the operator group"
    deploy_operator_group $template_dir $output_dir
    gum format --theme=light "## Deploying the prometheus operator"
    deploy_prometheus_operator $template_dir $output_dir

    gum format --theme=light "## Deploying the grafana operator"
    deploy_grafana_operator $template_dir $output_dir

    gum format --theme=light "## Deploying the grafana dashboard and datasource"
    deploy_grafana_datasource $template_dir $output_dir
    deploy_grafana_dashboard $template_dir $output_dir

    gum format --theme=light "## Deploying the grafana"
    deploy_grafana $template_dir $output_dir

}
