#! /bin/bash

scriptdir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
source VERSIONS

function push_to_bintray(){
cd $scriptdir
PACKAGE=$1
VERSION=$2
API_AUTH=$3
subject=jschnasse
repo=debian
filepath=${PACKAGE}_${VERSION}.deb
curl -u$API_AUTH -XPOST "https://bintray.com/api/v1/packages/$subject/$repo/" -d@bintray/${PACKAGE}/package.json -H"content-type:application/json"
curl -u$API_AUTH -XPOST "https://bintray.com/api/v1/packages/$subject/$repo/$PACKAGE/versions" -d@bintray/${PACKAGE}/version.json -H"content-type:application/json"
curl -u$API_AUTH -T deb/$filepath "https://bintray.com/api/v1/content/$subject/$repo/$PACKAGE/$VERSION/$filepath;deb_distribution=buster;deb_component=main;deb_architecture=all;publish=1;override=1;"
curl -u$API_AUTH -XPUT "https://bintray.com/api/ui/artifact/$subject/$repo/$filepath" -d'{"list_in_downloads":true}' -H"content-type:application/json"
cd -
}
apiauth=$1
push_to_bintray oi $oi_version $apiauth
push_to_bintray lscsv $lscsv_version $apiauth
push_to_bintray libprocname $libprocname_version $apiauth
