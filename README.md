# DayPricing

This is a sample application that is intended to provide a simple microservice to provide the pricing, if it is configured in the configuration. 

## How to use this 

Make a GET request to /findPrice/between/$DateOne$/$DateTwo$

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
  (Change the Accept for XML)
 
## How to execute this in Docker

To build the docker container. Go to the base of the project directory. Issue the following command:

    docker build -t testbuild .
    
    (Subsitute testbuild for the name of the docker image you're building]
    
    To run: docker run -v $(pwd)/application.json:/daypricing/application.json -p 8080:8080 testbuild
 


## Guide on deploying in GCP 

[Through the command line tools GCP supports docker deployments.] (https://cloud.google.com/kubernetes-engine/docs/tutorials/hello-app)

