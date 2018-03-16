FROM openjdk:8-jre
COPY target/daypricing-1.0-SNAPSHOT-allinone.jar /daypricing/
WORKDIR /daypricing
CMD ["java","-Dconfig=/daypricing/app.json","-jar","/daypricing/daypricing-1.0-SNAPSHOT-allinone.jar"]

