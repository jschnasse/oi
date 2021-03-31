[![Build Status](https://travis-ci.org/jschnasse/oi.svg?branch=master)](https://travis-ci.org/jschnasse/oi)
[![codecov](https://codecov.io/gh/jschnasse/oi/branch/master/graph/badge.svg)](https://codecov.io/gh/jschnasse/oi)

# About

`oi` a small tool to convert between csv,json,yml,xml,rdf. 
The tool attempts to create various output formats. 
The result is not meant to be 100% correct for each and every case. 
The overall idea is to provide adhoc conversions just as one step in a conversion pipeline.
It can be combined easily with existing tools like `xsltproc`,`jq`,`sed`,`grep` and `awk`.

# Install

```
wget https://dl.bintray.com/jschnasse/debian/oi_0.6.10.deb
sudo apt install ./oi_0.6.10.deb 
```

# Keep up to date

Accept the bintray gpg key. 

```
curl -sSL \
'https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x379ce192d401ab61' \
| sudo apt-key add -
```

Add [my repo](https://dl.bintray.com/jschnasse/debian/)

```
echo "deb https://dl.bintray.com/jschnasse/debian buster main" | sudo tee -a /etc/apt/sources.list.d/jschnasse.list
sudo apt update
sudo apt install oi
```

# Example Usage 

1. Convert `passwd` to `yml` (use `-t` to print different formats)

```
oi /etc/passwd -d":" --header="login,password,uid,gid,comment,home,shell" -icsv
```

2. List some disk info in `yml` (use `-t` to print different formats)

```
lsblk |grep -o sd.*|awk '{print $1 ";" $4 ";" $NF}'|oi -d";" --header="device,size,mount" -icsv
```

3. Create adhoc Json-Ld Context for some arbitrary Json-API

```
curl https://api.github.com/users/jschnasse|oi -ijson -tcontext
```

4. Generate pretty Json from RDF

```
oi -i turtle -t json books.ttl |jq '.["@graph"][0]'
``` 

with books.ttl

```
_:b0 a <http://schema.org/Book> ;
    <http://schema.org/name> "Semantic Web Primer (First Edition)" ;
    <http://schema.org/offers> _:b1 ;
    <http://schema.org/publisher> "Linked Data Tools" .

_:b1 a <http://schema.org/Offer> ;
    <http://schema.org/price> "2.95" ;
    <http://schema.org/priceCurrency> "USD" .
```

# Manual

```
$ oi --help
Usage: oi [-hvV] [-vv] [-d=Delimiter] [-f=JsonLdFrame] [--header=HeaderFields]
          [-i=<inputType>] [-o=<type>] [-q=QuoteChar] [<inputFile>]
Converts yaml,json,xml,rdf to each other.
      [<inputFile>]         Input file.
  -d, --delimiter=Delimiter delimiter for csv
  -f, --frame=JsonLdFrame   A json-ld Frame
  -h, --help                Show this help message and exit.
      --header=HeaderFields a comma separated list of headers
  -i, --inputType=<inputType>
                            yml,json,xml,rdf,context,csv,nt,turtle,ntriples,
                              jsonld
  -o, -t, --type, --outputType=<type>
                            yml,json,xml,rdf,context,csv,nt,turtle,ntriples,
                              jsonld,jsonschema
  -q, --quoteChar=QuoteChar quote char for csv
  -v, --verbose             Increase Verbosity to Warn
  -V, --version             Print version information and exit.
      -vv                   Increase Verbosity to Debug


```

Visit the man-page for more info.

# Clone and Install from Source

Requires: JRE 11, Git, Docker and asciidoctor

**Basic Install**

```
git clone https://github.com/jschnasse/oi
cd oi
mvn package
sudo cp src/main/resources/oi /usr/bin
sudo cp target/oi.jar /usr/lib
```

**Take a look at the script!**

```
sudo editor /usr/bin/oi
# You can modify the _java command,e.g. 
# set it to 'java'.
# Per default the tool comes with
# its own jvm. See next step.
```

**Install minimal jvm**

```
docker build -t adopt_jdk_image -f Dockerfile.build .
docker create --name adopt_jdk_container adopt_jdk_image
docker cp adopt_jdk_container:/opt/jvm_for_oi /tmp/jvm_for_oi
docker rm adopt_jdk_container

sudo mv /tmp/jvm_for_oi /usr/share
ln -s /usr/share/jvm_for_oi/bin/java /usr/bin/jvm_for_oi
```

**Install man page**

```
cd man
asciidoctor -b manpage man.adoc
sudo cp oi.1 /usr/share/man/man1/
sudo mandb
```

# Detailed Usage Examples

Find examples in the [test directory](https://github.com/jschnasse/oi/tree/master/src/test/resources). 
Each test folder contains a `in`,`out` and `context` dir. The `in` dir contains the original source.
The `context` dir contains a generated Json-Ld context. The `out` dir contains generated files. The
`jsonld` files are generated with the aim of the context files from the `context` dir. 

To test the examples by yourself you have to (1) clone this repo, (2) install the oi tool and 
(3) cd into `src/test/resources` directory.



## Navigate to Test Folder

In the oi repo folder:

```
cd src/test/resources
```

## CSV Examples

```
oi csv/in/BesucherzahlenMuseen2019.csv -d=";" 
oi csv/in/BesucherzahlenMuseen2019.csv -d=";" -t json 
oi csv/in/BesucherzahlenMuseen2019.csv -d=";" -t xml 
```
## Json Examples

```
oi json/in/rosenmontag.json -t context
oi json/in/rosenmontag.json -f json/context/rosenmontag.json.context -trdf
oi json/in/rosenmontag.json 
oi json/in/rosenmontag.json -tjson
oi json/in/rosenmontag.json -txml 
```
## Rdf

You can use `oi` to create adhoc conversions of other structured text formats to
rdf. `oi` prefers Json-Ld as rdf serialization format and utilizes tools from Json-Ld
to create rdf.

Existing documents can be converted to rdf using a Json-Ld Frame. A Json-Ld Frame is a 
json file that contains information on how to interpret an existing hierarchical structure
to create valid and nice looking Json-Ld Documents.

## Use oi to create rdf

Run `oi` to create adhoc rdf.

This is as easy as:

```
oi json/in/rosenmontag.json -trdf
```
What happens here? Internally an adhoc context is created
that is later used to constructed the rdf.

Here are the details. Let's start with:

```
cd oi/src/test/resources
oi json/in/rosenmontag.json -t context
```

This will print something like

```
{
  "@context" : {
    "type" : {
      "@id" : "info:oi/type"
    },
    "crs" : {
      "@id" : "info:oi/crs"
    },
    "properties" : {
      "@id" : "info:oi/properties"
    },
...
    "Shape_Length" : {
      "@id" : "info:oi/Shape_Length"
    }
  }
}
 
```

One can use this output to create a simple Json-Ld Frame. Just add the following line in
before or after the @context object.

```
{
  "type": "FeatureCollection",
  @context 
....
```

Save it, e.g. under `/tmp/frame` and create your json-ld with

```
oi json/in/rosenmontag.json -f /tmp/frame -trdf
```

# Create Release

**Test**

```
mvn package
docker build -t docker_build_test -f docker/Dockerfile.build.test .
docker build -t docker_build_convert__test -f docker/Dockerfile.buildAndConvert.test .
```

**Bump Versions**

```
editor VERSIONS
source VERSIONS
./bumpVersion.sh
```

**Commit, Push, Git** 

```
git add deb/*
git add .
git commit -m "Prepare for release ${oi_version}"
mvn gitflow:release
```

**Build**

```
./build.sh 
```

**Release**

```
./push_to_bintray.sh {{user}}:{{token}}
```

**Test**

```
docker build -t docker_install_test -f docker/Dockerfile.install.test .
docker build -t docker_install_convert_test -f docker/Dockerfile.installAndConvert.test .
```

# Create Debian Package

This will create a debian package under `./deb`

```
build.sh
```

# Docker Tests

**Test if build is working**

```
docker build -t docker_build_test -f docker/Dockerfile.build.test .
```

**Build plus conversion**

```
docker build -t docker_build_convert_test -f docker/Dockerfile.buildAndConvert.test .
```

**Test if install from repo works**

```
docker build -t docker_install_test -f docker/Dockerfile.install.test .
```

**Install plus conversion**

```
docker build -t docker_install_convert_test -f docker/Dockerfile.installAndConvert.test .
```
