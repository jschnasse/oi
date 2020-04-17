#! /bin/bash

PROJECT_PATH=/home/jan/development/cjxy

function build(){
 (
 cd $PROJECT_PATH
 mvn package
 sudo cp src/main/resources/cjxy /usr/bin
 sudo cp target/cjxy.jar /usr/lib
 cd man
 asciidoctor -b manpage man.adoc
 sudo cp cjxy.1 /usr/share/man/man1/
 sudo mandb
 )

}

function generateCsv(){
	mkdir -p csv/out
	mkdir -p csv/context
	cjxy csv/in/$1 -d=";" -t context > csv/context/$1.context
	cjxy csv/in/$1 -d=";" > csv/out/$1.yml
	cjxy csv/in/$1 -d=";" -t json > csv/out/$1.json
	cjxy csv/in/$1 -d=";" -t xml > csv/out/$1.xml
	cjxy csv/in/$1 -d=";" -f csv/context/$1.context -trdf > csv/out/$1.jsonld
}

function generateJson(){
	mkdir -p json/out
	mkdir -p json/context
	cjxy json/in/$1 -t context > json/context/$1.context
	cjxy json/in/$1 > json/out/$1.yml
	cjxy json/in/$1 -txml > json/out/$1.xml
	cjxy json/in/$1 -tcsv > json/out/$1.csv
	cjxy json/in/$1 -f json/context/$1.context -trdf > json/out/$1.jsonld
}

function generateRdf(){
	mkdir -p rdf/out
	mkdir -p rdf/context
	cjxy rdf/in/$1 -t context > rdf/context/$1.context
	cjxy rdf/in/$1 > rdf/out/$1.yml
	cjxy rdf/in/$1 -tjson > rdf/out/$1.json
	cjxy rdf/in/$1 -txml > rdf/out/$1.xml
	cjxy rdf/in/$1 -tcsv > rdf/out/$1.csv
	cjxy rdf/in/$1 -f rdf/context/$1.context -trdf > rdf/out/$1.jsonld
}

function generateXml(){
	mkdir -p xml/out
	mkdir -p xml/context
	cjxy xml/in/$1 -d=";" -t context > xml/context/$1.context
	cjxy xml/in/$1 -d=";" > xml/out/$1.yml
	cjxy xml/in/$1 -d=";" -tjson > xml/out/$1.json
	cjxy xml/in/$1 -d=";" -tcsv > xml/out/$1.csv
	cjxy xml/in/$1 -d=";" -f xml/context/$1.context -trdf > xml/out/$1.jsonld
}

build

cd $PROJECT_PATH/src/test/resources


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
