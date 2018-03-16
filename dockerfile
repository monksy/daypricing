FROM openjdk:8-jre
COPY target/daypricing-1.0-SNAPSHOT-allinone.jar /daypricing/
WORKDIR /daypricing
RUN touch /daypricing/application.json

CMD ["java","-Dconfig.file=/daypricing/application.json","-jar","/daypricing/daypricing-1.0-SNAPSHOT-allinone.jar"]

