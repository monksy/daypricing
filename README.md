# DayPricing

This is a sample application that is intended to provide a simple microservice to provide the pricing, if it is configured in the configuration. 

## How to use this 

Make a GET request to /findPrice/between/$DateOne$/$DateTwo$

### Inputs

|-----------|------------|------------|
| Inputs   |  Example    | Description| 
|-----------|------------|------|
| DateOne (In url) | 2015-07-04T12:00:00Z | This is a date where the search starts | 
| DateTwo (in url) | 2015-07-04T12:00:00Z | This is a newer date where the search ends | 

### Outputs 

Normal outputs: HTTP200 for normal outputs and given the Content-Type: 

**JSON**

The result represents the price:

   { result: "1500" }

**XML**
   
   &lt;result>1500&lt;/result>
      
## How to execute this in Docker

**LEFT TODO**


### Items left todo

 * Create Core 
   * Configuration
   * Config Case Class
   * Create test for the sample cases
 * Interface
   * Health Endpoint 
     * Ok
     * Test
     * XML Output + Test
     * Json Output + Test
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
 * Create Docker container
 * Update Readme 
   * Curl Commands used to invoke the service
   * Docker command to run the container 
 * Review requirements to show that this is inline 