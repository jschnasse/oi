#! /bin/bash

mvn clean package
mkdir target/deb
cp target/cjxy.jar target/deb
/opt/jdk-14/bin/jpackage --name cjxy --input target/deb --main-jar cjxy.jar
