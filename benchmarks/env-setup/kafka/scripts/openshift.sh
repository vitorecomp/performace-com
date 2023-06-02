
generate_operator_group_yaml() {
    template_dir=$1
    output_dir=$2
}

apply_operator_group() {
    deploy_dir=$1
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
}

apply_operator() {
    deploy_dir=$1
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
}

apply_kafka() {
    deploy_dir=$1
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
}

apply_topic() {
    deploy_dir=$1
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
}

apply_monitoring() {
    deploy_dir=$1
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

    gum format --theme=light "## Deploying the kafka crd"
    deploy_kafka $template_dir $output_dir

    gum format --theme=light "## Deploying kafka topic"
    deploy_topic $template_dir $output_dir

    gum format --theme=light "## Deploying kafka monitoring tools"
    deploy_monitoring $template_dir $output_dir
}

