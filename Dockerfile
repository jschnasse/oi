FROM adoptopenjdk/openjdk12:jdk-12.0.1_12 as build
RUN jlink \
    --add-modules java.base \
    --verbose \
    --strip-debug \
    --compress 2 \
    --no-header-files \
    --no-man-pages \
    --output /opt/jvm_for_oi
    
FROM panga/alpine:3.8-glibc2.27

LABEL maintainer="Jan Schnasse"

COPY --from=build /opt/jvm_for_oi /usr/share/jvm_for_oi

COPY target/oi.jar /usr/lib
COPY src/main/resources/oi /usr/bin
RUN ln -s /usr/share/jvm_for_oi/bin/java /usr/bin/jvm_for_oi \
&& chmod +x /usr/bin/oi \
&& oi /etc/passwd -d":" --header="login,password,uid,gid,comment,home,shell" -icsv
