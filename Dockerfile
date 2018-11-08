FROM centos

VOLUME /tmp

ADD jdk-8u191-linux-x64.tar.gz /usr/local/
ENV JAVA_HOME /usr/local/jdk1.8.0_191
ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
ENV PATH $PATH:$JAVA_HOME/bin

COPY lib /lib
ADD monitor-core-1.0.jar /monitor-core.jar

EXPOSE 8084
ENTRYPOINT ["java","-jar","/monitor-core.jar"]
