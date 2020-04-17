# About
`cjxy` a small tool to convert csv,json,yaml,xml,rdf to each other.
Use `cjxy` to translate one structured format into another. Even if
the result is not one hundred percent correct it almost always 
can be fixed easily with existing tools like `sed`,`grep` and `awk`.

## Some interesting calls

1. Convert passwd to yml (use `-t` to print different formats)

```
cjxy /etc/passwd -d":" --header="login,password,uid,gid,comment,home,shell" -icsv
```

2. List some disk info in yml (use `-t` to print different formats)

```
lsblk |grep -o sd.*|awk '{print $1 ";" $4 ";" $NF}'|cjxy -d";" --header="device,size,mount" -icsv
```

# Examples

Find examples in the [test directory](https://github.com/jschnasse/cjxy/tree/master/src/test/resources). 
Each test folder contains a `in`,`out` and `context` dir. The `in` dir contains the original source.
The `context` dir contains a generated Json-Ld context. The `out` dir contains generated files. The
`jsonld` files are generated with the aim of the context files from the `context` dir. 

To test the examples by yourself you have to (1) clone this repo, (2) install the cjxy tool and 
(3) cd into `src/test/resources` directory.

## Clone and Install

Requires: JRE >1.8, Git and asciidoctor

```
git clone https://github.com/jschnasse/cjxy
cd cjxy
mvn package
sudo cp src/main/resources/cjxy /usr/bin
sudo cp target/cjxy.jar /usr/lib
cd man
asciidoctor -b manpage man.adoc
sudo cp cjxy.1 /usr/share/man/man1/
sudo mandb

```

## Navigate to Test Folder

In the cjxy repo folder:

```
cd src/test/resources
```

## CSV Examples

```
cjxy csv/in/BesucherzahlenMuseen2019.csv -d=";" 
cjxy csv/in/BesucherzahlenMuseen2019.csv -d=";" -t json 
cjxy csv/in/BesucherzahlenMuseen2019.csv -d=";" -t xml 
```
## Json Examples

```
cjxy json/in/rosenmontag.json -t context
cjxy json/in/rosenmontag.json -f json/context/rosenmontag.json.context -trdf
cjxy json/in/rosenmontag.json 
cjxy json/in/rosenmontag.json -tjson
cjxy json/in/rosenmontag.json -txml 
```
## Rdf

You can use `cjxy` to create adhoc conversions of other structured text formats to
rdf. `cjxy` prefers Json-Ld as rdf serialization format and utilizes tools from Json-Ld
to create rdf.

Existing documents can be converted to rdf using a Json-Ld Frame. A Json-Ld Frame is a 
json file that contains information on how to interpret an existing hierarchical structure
to create valid and nice looking Json-Ld Documents.

## Use cjxy to create rdf

Run `cjxy` to create adhoc context.

```
cd cjxy/src/test/resources
cjxy json/in/rosenmontag.json -t context
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
cjxy json/in/rosenmontag.json -f /tmp/frame -trdf
```

## Json-Ld

To use rdf and json-ld conversion, it is mandatory to provide a [Json-Ld Frame](https://w3c.github.io/json-ld-framing/).
Example

```
cd src/test/resources/json
cjxy HT015847062.json -f frame.json -trdf
# You will observe, that the output is Json-Ld again
cjxy HT015847062.rdf -f frame.json -trdf
# Also in this case you get Json-Ld back
```


# Install with

Requires: JRE >1.8

```
git clone https://github.com/jschnasse/cjxy
cd cjxy
mvn package
sudo cp src/main/resources/cjxy /usr/bin
sudo cp target/cjxy.jar /usr/lib
cd man
asciidoctor -b manpage man.adoc
sudo cp cjxy.1 /usr/share/man/man1/
sudo mandb
```

# Call with

```
$ cjxy --help
Usage: cjxy [-hV] [-d=Delimiter] [-f=JsonLdFrame] [--header=HeaderFields]
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


# Create Debian Package
```
editor package.sh # provide valid path to jpackage program
./package.sh
sudo dpkg -i cjxy_1.0-1_amd64.deb
```
