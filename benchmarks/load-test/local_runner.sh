#getting the script dir
dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

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
	'Perfomance Bench' "Running a benckmark!"

#test if the environment name is provided
name=$1
if [ -z "$name" ]; then
    name=$(gum choose "kafka-http-burst" "slow-dependecy")
fi


mkdir -p $dir/tmp

if [ "$name" == "kafka-http-burst" ]; then
    cd $dir/kafka-http-burst
    k6 run integration-run.js
elif [ "$name" == "slow-dependecy" ]; then
    echo "slow-dependecy was no implemented it."
    echo "fell free to contribute, any help is welcome."
    exit
else
    echo "Invalid option"
    exit
fi
