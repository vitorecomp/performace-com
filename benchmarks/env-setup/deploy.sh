#!/bin/bash
###################################### main ######################################

#getting the script dir
dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

#look if is necessary to clean the project
if [ "$2" == "clean" ]; then
    clean=true
else
    clean=false
fi

#look if is necessary to clean the project
if [ "$3" == "dedicated" ]; then
    dedicated=true
else
    dedicated=false
fi

# test if gum is installed
if ! command -v gum &> /dev/null
then
    echo "gum could not be found. Please install gum to continue."
    echo "go install github.com/charmbracelet/gum@latest"
    exit
fi

gum style \
	 --foreground 69 --border-foreground 156 --border thick \
	--align center --width 50 --margin "1 2" --padding "2 4" \
	'Perfomance Bench' "Instaling the kafka product"

#test if the environment name is provided
name=$1
if [ -z "$name" ]; then
    name=$(gum choose "kubernetes" "openshift")
fi


mkdir -p $dir/tmp

if [ "$name" == "kubernetes" ]; then
    echo "Kubernetes was no implemented it."
    echo "fell free to contribute, any help is welcome."
    exit
else
    sh deploy-openshift.sh $clean $dedicated
fi