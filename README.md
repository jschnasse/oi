A small tool to convert json,yaml,xml,rdf to each other.

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
```

# Call with

```
$ cjxy --help
Usage: cjxy [-hV] [-t=<type>] <inputFile>
Converts yaml,json,xml to each other.
      <inputFile>     Input file.
  -h, --help          Show this help message and exit.
  -t, --type=<type>   yaml,json,xml
  -V, --version       Print version information and exit.


```

# Create Debian Package
```
editor package.sh # provide valid path to jpackage program
./package.sh
sudo dpkg -i cjxy_1.0-1_amd64.deb
```
