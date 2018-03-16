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
 
 
## How to execute this in Docker

To build the docker container. Go to the base of the project directory. Issue the following command:

    docker build -t testbuild .
    
    (Subsitute testbuild for the name of the docker image you're building]
    
    To run: docker run -v application.json:/daypricing/app.json -p 8080:8080 testbuild
 


## Guide on deploying in GCP 

[Through the command line tools GCP supports docker deployments.] (https://cloud.google.com/kubernetes-engine/docs/tutorials/hello-app)


### Items left todo

 * Interface
   * Find price endpoint
     * Define endpoint + test
     * Wrap price 
     * Create Test 
     * Create Test for Json
     * Create Test for XML
     * Reproduce all calls for the examples given
   * Update Health Check
     * Number of calls
     * Average Response time 
     * Update Tests
 * Review requirements to show that this is inline 