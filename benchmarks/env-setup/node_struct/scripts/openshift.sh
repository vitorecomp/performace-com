run_openshift() {
    dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"
    source $dir/../../utils/openshift-utils.sh

    #validate if a file extist
    if [ ! -f "$dir/../node_struct.json" ]; then
        echo "The file $dir/../node_struct.json does not exists."
        echo "Please configure the file with the node_struct_bkp.json structure."
        exit
    fi
    gum format --theme=light "# Defining the node struture"

    gum format --theme=light "## Deploying kafka"
    kafka_nodes=$(cat $dir/../node_struct.json | jq -r ".kafka[]")
    for node in $kafka_nodes; do
        gum format --theme=light "### Deploying the node kafka selector for $node"
        set_node_selector $node "kafka"
        gum format --theme=light "### Deploying the node kafka taint for $node"
        set_node_taint $node "kafka"
    done

    gum format --theme=light "## Deploying kafka_utils"
    kafka_utils=$(cat $dir/../node_struct.json | jq -r ".kafka_utils[]")
    for node in $kafka_utils; do
        gum format --theme=light "### Deploying the node kafka_utils selector for $node"
        set_node_selector $node "kafka-utils"
        gum format --theme=light "### Deploying the node kafka_utils taint for $node"
        set_node_taint $node "kafka-utils"
    done

    gum format --theme=light "## Deploying postgres"
    postgres=$(cat $dir/../node_struct.json | jq -r ".postgres[]")
    for node in $postgres; do
        gum format --theme=light "### Deploying the node postgres selector for $node"
        set_node_selector $node "postgres"
        gum format --theme=light "### Deploying the node postgres taint for $node"
        set_node_taint $node "postgres"
    done

    gum format --theme=light "## Deploying application"
    application=$(cat $dir/../node_struct.json | jq -r ".application[]")
    for node in $application; do
        gum format --theme=light "### Deploying the node application selector for $node"
        set_node_selector $node "application"
        gum format --theme=light "### Deploying the node application taint for $node"
        set_node_taint $node "application"
    done

}