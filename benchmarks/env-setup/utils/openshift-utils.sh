create_project() {
    project_name=$1
    clean=$2

    if oc get project $project_name &> /dev/null;
    then
        if $clean; then
                oc delete project $project_name
             gum spin --title "Deleting project $project_name" -- oc wait --for=delete --timeout=600s namespace/$project_name
        else
            gum format --theme=pink "### $project_name project already exist. Finishing the installation process."
            gum format --theme=pink "### If you want to reinstall postgres, please delete the project $project_name and run this script again."
            gum format --theme=pink "### oc delete project $project_name"
            exit
        fi
    fi

    #create the project
    oc new-project $project_name
}
set_node_taint() {
    node=$1
    taints=$2

    oc adm taint nodes $node app=$taints:NoSchedule --overwrite
}

set_node_selector() {
    node=$1
    selector=$2

    oc label node $node node/type=$selector --overwrite
}

set_project_selector(){
    project_name=$1
    selector=$2

    oc label namespace $project_name $selector
}