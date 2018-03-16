FROM openjdk:8-jre
COPY target/daypricing-1.0-SNAPSHOT-allinone.jar /daypricing/
WORKDIR /daypricing
CMD ["java","-Dconfig=/daypricing/application.json","-jar","/daypricing/daypricing-1.0-SNAPSHOT-allinone.jar"]

