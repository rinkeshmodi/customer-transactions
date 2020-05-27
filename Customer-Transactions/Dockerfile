FROM openjdk:8

RUN mkdir -p /opt/service

ENV PROFILE=prod

ENV PORT 8000

ENV JAR_FILE target/Customer-Transactions-0.0.1-SNAPSHOT.jar

COPY target/Customer-Transactions-0.0.1-SNAPSHOT.jar /opt/service/

WORKDIR /opt/service

CMD ["java", "-Xmx3g", "-Xms1g", "-jar", "-Dprofile=prod", "-Dserver.port=8000", "/opt/service/Customer-Transactions-0.0.1-SNAPSHOT.jar"]
