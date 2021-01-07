#! /bin/bash

scriptdir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $scriptdir
source VERSIONS
cat VERSIONS

if [ ! -d deb/oi_${oi_version} ]
then
#echo "Create deb directory on bases of last version!"
#echo "cp -r $(ls -tdr deb/oi*/|tail -1) deb/oi_${oi_version}"
#cp -r $(ls -tdr deb/oi*/|tail -1) deb/oi_${oi_version}
    echo "Create deb directory!"
    cp -r deb/oi deb/oi_${oi_version}
fi

#README
sed -i s/"oi_[0-9]*\.[0-9]*\.[0-9]*\.deb"/"oi_${oi_version}.deb"/ README.md

#CONTROL
sed -i s/"^Version:.*"/"Version: ${oi_version}"/ deb/oi_${oi_version}/DEBIAN/control

#man page
sed -i s/"^v[0-9]*\.[0-9]*\.[0-9]*"/"v${oi_version}"/ man/oi/man.adoc

#Main.java
sed -i s/"oi [0-9]*\.[0-9]*\.[0-9]*"/"oi ${oi_version}"/ src/main/java/org/schnasse/oi/main/Main.java

#Maven
sed -i s/"<version>.*[0-9]*\.[0-9]*\.[0-9]*-SNAPSHOT"/"<version>${oi_version}-SNAPSHOT"/ pom.xml
