
generate_operator_group_yaml() {
    template_dir=$1
    output_dir=$2

    template_dir=$1
    output_dir=$2

    envsubst <$template_dir/kafka-operator-group-template.yaml >|$output_dir/kafka-operator-group.yaml
}

apply_operator_group() {
    deploy_dir=$1

    oc apply -f $deploy_dir/kafka-operator-group.yaml
}

deploy_operator_group() {
    template_dir=$1
    output_dir=$2

    generate_operator_group_yaml $template_dir $output_dir
    apply_operator_group $output_dir
}

generate_operator_yaml() {
    template_dir=$1
    output_dir=$2

    name=amq-streams
    catalog_display_name='Red Hat Operators'

    channel=$(oc get packagemanifest -o json | jq '.items[] | select(.metadata.name=="'$name'") | select(.status.catalogSourceDisplayName=="'"${catalog_display_name}"'")' | jq .status.defaultChannel)
    channel=$(echo $channel | sed 's/"//g')
    currentCSV=$(oc get packagemanifest -o json | jq '.items[] | select(.metadata.name=="'$name'") | select(.status.catalogSourceDisplayName=="'"${catalog_display_name}"'")'  | jq '.status.channels[] | select(.name=="'$channel'")' | jq .currentCSV)
    currentCSV=$(echo $currentCSV | sed 's/"//g')
    catalog_source=$(oc get packagemanifest -o json | jq '.items[] | select(.metadata.name=="'$name'") | select(.status.catalogSourceDisplayName=="'"${catalog_display_name}"'")' | jq .metadata.labels.catalog)
    catalog_source=$(echo $catalog_source | sed 's/"//g')
    catalog_source_namespace=$(oc get packagemanifest -o json | jq '.items[] | select(.metadata.name=="'$name'") | select(.status.catalogSourceDisplayName=="'"${catalog_display_name}"'")' | jq '.metadata.labels."catalog-namespace"')
    catalog_source_namespace=$(echo $catalog_source_namespace | sed 's/"//g')

    export name=$name
    export channel=$channel
    export currentCSV=$currentCSV
    export catalog_source=$catalog_source
    export catalog_source_namespace=$catalog_source_namespace

    envsubst <$template_dir/kafka-operator-template.yaml >|$output_dir/kafka-operator.yaml    
}

apply_operator() {
    deploy_dir=$1

    oc apply -f $deploy_dir/kafka-operator.yaml
}

deploy_operator() {
    template_dir=$1
    output_dir=$2

    generate_operator_yaml $template_dir $output_dir
    apply_operator $output_dir
}

generate_kafka_yaml() {
    template_dir=$1
    output_dir=$2

    envsubst <$template_dir/kafka-crd-template.yaml >|$output_dir/kafka-crd.yaml
}

apply_kafka() {
    deploy_dir=$1

    oc apply -f $deploy_dir/kafka-crd.yaml
}

deploy_kafka() {
    template_dir=$1
    output_dir=$2

    generate_kafka_yaml $template_dir $output_dir
    apply_kafka $output_dir
}

generate_topic_yaml() {
    template_dir=$1
    output_dir=$2

    envsubst <$template_dir/kafka-topic-template.yaml >|$output_dir/kafka-topic.yaml
}

apply_topic() {
    deploy_dir=$1

    oc apply -f $deploy_dir/kafka-topic.yaml
}

deploy_topic() {
    template_dir=$1
    output_dir=$2

    generate_topic_yaml $template_dir $output_dir
    apply_topic $output_dir
}

generate_monitoring_yaml() {
    template_dir=$1
    output_dir=$2

    cp $template_dir/kafka-metrics-template.yaml $output_dir/kafka-metrics.yaml
}

apply_monitoring() {
    deploy_dir=$1

    oc apply -f $deploy_dir/kafka-metrics.yaml
}

deploy_monitoring() {
    template_dir=$1
    output_dir=$2

    generate_monitoring_yaml $template_dir $output_dir
    apply_monitoring $output_dir
}

run_openshift() {
    template_dir=$1
    output_dir=$2
    clean=$3

    dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"
    source $dir/../../utils/openshift-utils.sh

    gum format --theme=light "# Deploying to openshift"
    gum format --theme=light "## Creating the project"

    create_project "kafka" $clean

    gum format --theme=light "## Creating the operator group"
    deploy_operator_group $template_dir $output_dir

    gum format --theme=light "## Deploying the kafka operator"
    deploy_operator $template_dir $output_dir

    gum format --theme=light "## Deploying kafka monitoring tools"
    deploy_monitoring $template_dir $output_dir

    gum format --theme=light "## Deploying the kafka crd"
    deploy_kafka $template_dir $output_dir

    gum format --theme=light "## Deploying kafka topic"
    deploy_topic $template_dir $output_dir

    
}