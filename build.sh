#! /bin/bash

scriptdir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $scriptdir
source VERSIONS
mvnparam=$1

function build_oi(){
 package_name=$1
 package_version=$2
 package=${package_name}_$package_version
 mkdir -p deb/$package/usr/lib
 mkdir -p deb/$package/usr/bin
 mkdir -p deb/$package/usr/share/man/man1/
 mvn package -D$mvnparam
 sudo cp src/main/resources/$package_name deb/$package/usr/bin
 sudo cp target/$package_name.jar deb/$package/usr/lib
 
 jlink \
    --add-modules java.base \
    --verbose \
    --strip-debug \
    --compress 2 \
    --no-header-files \
    --no-man-pages \
    --output deb/$package/usr/share/jvm_for_oi
 ln -s deb/$package/usr/share/jvm_for_oi deb/$package/usr/bin/jvm_for_oi 
 
}

function build(){
 package_name=$1
 package_version=$2
 package=${package_name}_$package_version

 if [ -d $scriptdir/man/$package_name ]
 then
   cd $scriptdir/man/$package_name
   asciidoctor -b manpage man.adoc
   cd -
   sudo cp $scriptdir/man/$package_name/$package_name.1 deb/$package/usr/share/man/man1/
 fi  
 dpkg-deb --build deb/$package
}

build_oi oi $oi_version
build oi $oi_version
build lscsv $lscsv_version
build libprocname $libprocname_version
