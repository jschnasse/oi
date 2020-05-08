#! /bin/bash

scriptdir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $scriptdir

function generateAll(){
  extension=$1
  for i in `ls $extension/in/`
  do
    generate $extension $i
  done
}

function generate(){
    extension=$1
    filename=$2
	mkdir -p $extension/out
	mkdir -p $extension/context
	oi $extension/in/$filename -d=";" -t context > $extension/context/$filename.context
	oi $extension/in/$filename -d=";" -t yaml > $extension/out/$filename.yml
	oi $extension/in/$filename -d=";" -t json > $extension/out/$filename.json
	oi $extension/in/$filename -d=";" -t xml > $extension/out/$filename.xml
	oi $extension/in/$filename -d=";" -t csv > $extension/out/$filename.csv
	oi $extension/in/$filename -d=";" -f $extension/context/$filename.context -trdf > $extension/out/$filename.jsonld
}


./build.sh -DskipTests
sudo dpkg -i deb/*.deb
cd $scriptdir/src/test/resources

generateAll csv
generateAll json
generateAll rdf
generateAll xml
generateAll yml
