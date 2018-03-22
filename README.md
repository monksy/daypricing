# DayPricing

This is a sample application that is intended to provide a simple microservice to provide the pricing, if it is configured in the configuration. 

## How to use this 

Make a GET request to /findPrice/between/$DateOne$/$DateTwo$

### Required Software Installed 

  * JRE 8.0 or higher
  * Maven 3.1+

### Inputs

| Inputs   |  Example    | Description| 
| ----------- |------------|------ |
| DateOne (In url) | 2015-07-04T12:00:00Z | This is a date where the search starts | 
| DateTwo (in url) | 2015-07-04T12:00:00Z | This is a newer date where the search ends | 

### Outputs 

Normal outputs: HTTP200 for normal outputs and given the Content-Type: 

**JSON**

The result represents the price:

   { result: "1500" }

**XML**
   
   &lt;result>1500&lt;/result>
      
      
### Curl Commands

 * HealthCheck- JSON- curl -v  'http://localhost:8080/healthcheck' -H 'Accept: application/json' 
 * HealthCheck- XML-  curl -v  'http://localhost:8080/healthcheck' -H 'Accept: application/xml'
 * FindPrices (unavailable) curl 'http://localhost:8080/findPrice/between/2015-07-04T07:00:00Z/2015-07-04T20:00:00Z' -H 'Accept: application/json'
 * FindPrices (2000, but in XML) curl 'http://localhost:8080/findPrice/between/2015-07-04T07:00:00Z/2015-07-04T12:00:00Z' -H 'Accept: application/xml'
  
  
## So you want to run this application (TL;DR version)

      git clone https://github.com/monksy/daypricing;
      cd daypricing;
      mvn package; 
      java -Dconfig.file=application.json -jar target/daypricing-1.0-SNAPSHOT-allinone.jar
      
 The application should start up with a message as such: 
 
     [INFO] [03/21/2018 21:24:17.698] [main] [App$(akka://my-system)] Starting the service at http://localhost:8080
 
## How to execute this application locally

To execute this application, navigate to the base of the project folder: (should be daypricing/) and execute the following command: 

    java -jar daypricing-1.0-SNAPSHOT-allinone.jar

This application uses a configurationfile file, by default application.json (this file is picked up in the last example because it is in the base of the project). To modify this use the config.file system property. An example of starting this application up with a configuration file of: BadConfig.json use this command.

    java -Dconfig.file=BadConfig.json  -jar daypricing-1.0-SNAPSHOT-allinone.jar
  

### Whats with the allinone jar? 

This is a fat jar that has all of the dependencies built in. This makes it easier to deploy the application via a container. 


### Expectations of the Configuration file 

An example of a configuration file is: 

    {
      "rates": [
        {
          "days": "mon,tues,wed,thurs,fri",
          "times": "0600-1800",
          "price": 1500
        },
        {
          "days": "sat,sun",
          "times": "0600-2000",
          "price": 2000
        }
      ]
    }

Invalid Configuration files will prevent the application from running. Examples of this are found by using the configuration files BadConfig.json and EmptyConfig.json.


## How to execute this in Docker

To build the docker container. Go to the base of the project directory. Issue the following command:

    docker build . -t testbuild
    
    (Subsitute testbuild for the name of the docker image you're building]
    
    To run: docker run -v $(pwd)/application.json:/daypricing/application.json -p 8080:8080 testbuild
 


## Guide on deploying in GCP 

[Through the command line tools GCP supports docker deployments.] (https://cloud.google.com/kubernetes-engine/docs/tutorials/hello-app)

