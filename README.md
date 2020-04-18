# About

`oi` a small tool to convert csv,json,yaml,xml,rdf to each other.
Use `oi` to translate one structured format into another. Even if
the result is not hundred percent correct it almost always 
can be fixed easily with existing tools like `sed`,`grep` and `awk`.

# Install

```
wget https://schnasse.org/deb/oi_0.0.1.deb
sudo dpkg -i oi_0.0.1.deb
```


# First try. 

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

# Manual

```
$ oi --help
Usage: oi [-hV] [-d=Delimiter] [-f=JsonLdFrame] [--header=HeaderFields]
            [-i=<inputType>] [-t=<type>] [<inputFile>]
Converts yaml,json,xml,rdf to each other.
      [<inputFile>]         Input file.
  -d, --delimiter=Delimiter delimiter for csv
  -f, --frame=JsonLdFrame   A json-ld Frame
  -h, --help                Show this help message and exit.
      --header=HeaderFields a comma separated list of headers
  -i, --inputType=<inputType>
                            yml,json,xml,rdf,context,csv
  -t, --type=<type>         yaml,json,xml,rdf,context,csv
  -V, --version             Print version information and exit.
```

Visit the man-page for more info.


# Detailed Examples

Find examples in the [test directory](https://github.com/jschnasse/oi/tree/master/src/test/resources). 
Each test folder contains a `in`,`out` and `context` dir. The `in` dir contains the original source.
The `context` dir contains a generated Json-Ld context. The `out` dir contains generated files. The
`jsonld` files are generated with the aim of the context files from the `context` dir. 

To test the examples by yourself you have to (1) clone this repo, (2) install the oi tool and 
(3) cd into `src/test/resources` directory.

## Clone and Install from Source

Requires: JRE >1.8, Git and asciidoctor

```
git clone https://github.com/jschnasse/oi
cd oi
mvn package
sudo cp src/main/resources/oi /usr/bin
sudo cp target/oi.jar /usr/lib
cd man
asciidoctor -b manpage man.adoc
sudo cp oi.1 /usr/share/man/man1/
sudo mandb

```

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

Run `oi` to create adhoc context.

```
cd oi/src/test/resources
oi json/in/rosenmontag.json -t context
```

This will print something like

```
{
  "@context" : {
    "type" : {
      "@id" : "info:jon-desktop/type"
    },
    "crs" : {
      "@id" : "info:jon-desktop/crs"
    },
    "properties" : {
      "@id" : "info:jon-desktop/properties"
    },
...
    "Shape_Length" : {
      "@id" : "info:jon-desktop/Shape_Length"
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

## Json-Ld

To use rdf and json-ld conversion, it is mandatory to provide a [Json-Ld Frame](https://w3c.github.io/json-ld-framing/).
Example

```
cd src/test/resources/json
oi HT015847062.json -f frame.json -trdf
# You will observe, that the output is Json-Ld again
oi HT015847062.rdf -f frame.json -trdf
# Also in this case you get Json-Ld back
```

# Install from source

Requires: JRE >1.8 and asciidoctor

```
git clone https://github.com/jschnasse/oi
cd oi
mvn package
sudo cp src/main/resources/oi /usr/bin
sudo cp target/oi.jar /usr/lib
cd man
asciidoctor -b manpage man.adoc
sudo cp oi.1 /usr/share/man/man1/
sudo mandb
```

# Create Debian Package
```
build.sh
```
