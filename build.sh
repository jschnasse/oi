#! /bin/bash

scriptdir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $scriptdir

function build(){
 mvn package
 sudo cp src/main/resources/cjxy deb/cjxy_0.1-1/usr/bin
 sudo cp target/cjxy.jar deb/cjxy_0.1-1/usr/lib
 cd $scriptdir/man
 asciidoctor -b manpage man.adoc
 cd -
 sudo cp $scriptdir/man/cjxy.1 deb/cjxy_0.1-1/usr/share/man/man1/
}
build
dpkg-deb --build deb/cjxy_0.1-1
