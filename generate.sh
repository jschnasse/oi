#! /bin/bash

scriptdir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $scriptdir

function generateAll(){
  extension=$1
  for i in `ls $scriptdir/src/test/resources/$extension/in/`
  do
    generate $extension $i
  done
}

function generate(){
    extension=$1
    filename=$2
    inputFile=$scriptdir/src/test/resources/$extension/in/$filename
    outputDir=$scriptdir/src/test/resources/$extension/out
    contextFile=$scriptdir/src/test/resources/$extension/context/$filename.context
	mkdir -p $outputDir
	mkdir -p $scriptdir/src/test/resources/$extension/context
	java -jar $scriptdir/target/oi.jar $inputFile -d=";" -t context > $contextFile
	java -jar $scriptdir/target/oi.jar $inputFile -d=";" -t yml > $outputDir/$filename.yml
	java -jar $scriptdir/target/oi.jar $inputFile -d=";" -t json > $outputDir/$filename.json
	java -jar $scriptdir/target/oi.jar $inputFile -d=";" -t xml > $outputDir/$filename.xml
	java -jar $scriptdir/target/oi.jar $inputFile -d=";" -t csv > $outputDir/$filename.csv
	java -jar $scriptdir/target/oi.jar $inputFile -d=";" -t rdf -f $contextFile > $outputDir/$filename.jsonld
}

mvn package -DskipTests

generateAll csv
generateAll json
generateAll rdf
generateAll xml
generateAll yml
