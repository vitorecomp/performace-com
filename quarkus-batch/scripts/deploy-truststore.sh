#!/bin/bash

name=$1

gum format --theme=pink "# Deploying the config map."

#if not name set as kafka
if [ -z "$name" ]
then
      name="kafka-cert"
fi

#get the script directory
dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

rm -r -f $dir/tmp
mkdir -p $dir/tmp

keytool -import -trustcacerts -alias root -file $dir/crt/$name.crt -keystore $dir/tmp/truststore.jks -storepass password -noprompt


if oc get project performance-bencher &> /dev/null;
then
    gum format --theme=pink "### performance-bencher project already exist. Finishing the installation process."
    oc project performance-bencher
else
    oc new-project performance-bencher
fi


#if config map already exist delete it
if oc get configmap kafka-truststore &> /dev/null;
then
    gum format --theme=pink "### Config map already exist. Deleting it."
    oc delete configmap kafka-truststore
fi

oc create configmap  kafka-truststore --from-file $dir/tmp/truststore.jks