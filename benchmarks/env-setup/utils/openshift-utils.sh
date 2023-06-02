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