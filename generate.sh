#! /bin/bash

scriptdir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $scriptdir

function generateCsv(){
	mkdir -p csv/out
	mkdir -p csv/context
	oi csv/in/$1 -d=";" -t context > csv/context/$1.context
	oi csv/in/$1 -d=";" > csv/out/$1.yml
	oi csv/in/$1 -d=";" -t json > csv/out/$1.json
	oi csv/in/$1 -d=";" -t xml > csv/out/$1.xml
	oi csv/in/$1 -d=";" -f csv/context/$1.context -trdf > csv/out/$1.jsonld
}

function generateJson(){
	mkdir -p json/out
	mkdir -p json/context
	oi json/in/$1 -t context > json/context/$1.context
	oi json/in/$1 > json/out/$1.yml
	oi json/in/$1 -txml > json/out/$1.xml
	oi json/in/$1 -tcsv > json/out/$1.csv
	oi json/in/$1 -f json/context/$1.context -trdf > json/out/$1.jsonld
}

function generateRdf(){
	mkdir -p rdf/out
	mkdir -p rdf/context
	oi rdf/in/$1 -t context > rdf/context/$1.context
	oi rdf/in/$1 > rdf/out/$1.yml
	oi rdf/in/$1 -tjson > rdf/out/$1.json
	oi rdf/in/$1 -txml > rdf/out/$1.xml
	oi rdf/in/$1 -tcsv > rdf/out/$1.csv
	oi rdf/in/$1 -f rdf/context/$1.context -trdf > rdf/out/$1.jsonld
}

function generateXml(){
	mkdir -p xml/out
	mkdir -p xml/context
	oi xml/in/$1 -d=";" -t context > xml/context/$1.context
	oi xml/in/$1 -d=";" > xml/out/$1.yml
	oi xml/in/$1 -d=";" -tjson > xml/out/$1.json
	oi xml/in/$1 -d=";" -tcsv > xml/out/$1.csv
	oi xml/in/$1 -d=";" -f xml/context/$1.context -trdf > xml/out/$1.jsonld
}


./build.sh
sudo dpkg -i deb/*.deb
cd $scriptdir/src/test/resources


for i in `ls csv/in/`
do
generateCsv $i
done

for i in `ls json/in/`
do
generateJson $i
done

for i in `ls rdf/in/`
do
generateRdf $i
done

for i in `ls xml/in/`
do
generateXml $i
done
