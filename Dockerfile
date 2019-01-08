FROM centos

VOLUME /tmp

ADD jdk-8u191-linux-x64.tar.gz /usr/local/
ENV JAVA_HOME /usr/local/jdk1.8.0_191
ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
ENV PATH $PATH:$JAVA_HOME/bin

COPY lib /lib
ADD monitor-core-1.0.jar /monitor-core.jar

EXPOSE 8084
EXPOSE 30005

ENTRYPOINT ["java",\
            "-Djava.rmi.server.hostname=47.94.157.199",\
            "-Dcom.sun.management.jmxremote=true",\
            "-Dcom.sun.management.jmxremote.port=30005",\
            "-Dcom.sun.management.jmxremote.rmi.port=30005",\
            "-Dcom.sun.management.jmxremote.ssl=false",\
            "-Dcom.sun.management.jmxremote.authenticate=false",\
            "-Dcom.sun.management.jmxremote.local.only=false",\
            "-jar","/monitor-core.jar"]
