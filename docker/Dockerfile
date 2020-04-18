FROM ubuntu:18.04
RUN apt-get update && apt-get install -y \
  wget \
  man \
  && wget https://schnasse.org/deb/oi_0.0.1.deb \
  && apt install -y ./oi_0.0.1.deb \
  && java -version \
  && oi /etc/passwd -d":" --header="login,password,uid,gid,comment,home,shell" -icsv

