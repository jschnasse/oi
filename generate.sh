#! /bin/bash

scriptdir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $scriptdir
source VERSIONS

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
    jvm_for_oi=deb/oi_$oi_version/usr/share/jvm_for_oi/bin/java
	mkdir -p $outputDir
	mkdir -p $scriptdir/src/test/resources/$extension/context
	$jvm_for_oi -jar $scriptdir/target/oi.jar -i $extension $inputFile -d=";" -t context > $contextFile
	$jvm_for_oi -jar $scriptdir/target/oi.jar -i $extension $inputFile -d=";" -t yml > $outputDir/$filename.yml
	$jvm_for_oi -jar $scriptdir/target/oi.jar -i $extension $inputFile -d=";" -t json > $outputDir/$filename.json
	$jvm_for_oi -jar $scriptdir/target/oi.jar -i $extension $inputFile -d=";" -t xml > $outputDir/$filename.xml
	$jvm_for_oi -jar $scriptdir/target/oi.jar -i $extension $inputFile -d=";" -t jsonld > $outputDir/$filename.jsonld
	$jvm_for_oi -jar $scriptdir/target/oi.jar -i $extension $inputFile -d=";" -t rdf > $outputDir/$filename.rdf
	$jvm_for_oi -jar $scriptdir/target/oi.jar -i $extension $inputFile -d=";" -t turtle > $outputDir/$filename.turtle
	$jvm_for_oi -jar $scriptdir/target/oi.jar -i $extension $inputFile -d=";" -t ntriples > $outputDir/$filename.nt
	$jvm_for_oi -jar $scriptdir/target/oi.jar -i $extension $inputFile -d=";" -o jsonschema > $outputDir/$filename.schema.json
}

function printHelp(){
   deb/oi_$oi_version/usr/share/jvm_for_oi/bin/java -jar $scriptdir/target/oi.jar --help
}

mvn package -DskipTests

generateAll csv
generateAll json
generateAll rdfxml
generateAll turtle
generateAll xml
generateAll yml

printHelp

