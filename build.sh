#! /bin/bash

scriptdir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $scriptdir

package=oi_0.0.1

function build(){
 mkdir -p deb/$package/usr/lib
 mkdir -p deb/$package/usr/bin
 mkdir -p deb/$package/usr/share/man/man1/
 mvn package $1
 sudo cp src/main/resources/oi deb/$package/usr/bin
 sudo cp target/oi.jar deb/$package/usr/lib
 cd $scriptdir/man
 asciidoctor -b manpage man.adoc
 cd -
 sudo cp $scriptdir/man/oi.1 deb/$package/usr/share/man/man1/
}

build $1
dpkg-deb --build deb/$package
