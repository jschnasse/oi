FROM adoptopenjdk/openjdk12:jdk-12.0.1_12 as build
RUN jlink \
    --add-modules java.base \
    --verbose \
    --strip-debug \
    --compress 2 \
    --no-header-files \
    --no-man-pages \
    --output /opt/java/jdk
    
FROM panga/alpine:3.8-glibc2.27

LABEL maintainer="Jan Schnasse"

COPY --from=build /opt/java/jdk /opt/java/jdk

COPY target/oi.jar /usr/lib
COPY src/main/resources/oi /usr/bin
RUN ln -s /opt/java/jdk/bin/java /usr/bin \
&& chmod +x /usr/bin/oi \
&& oi /etc/passwd -d":" --header="login,password,uid,gid,comment,home,shell" -icsv
