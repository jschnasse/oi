A small tool to convert json,yaml,xml to each other

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
