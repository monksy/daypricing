package com.mrmonksy.daypricing

import java.io.File

import akka.http.scaladsl.model.{HttpHeader, StatusCode, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers, WordSpec}

class AppTest extends WordSpec with Matchers with ScalatestRouteTest with ServiceRoutes {

  "Webservice::/healthcheck" should {
    "return the result for a health check in JSON (the default)" in {
      Get("/healthcheck") ~> route ~> check {
        status should be(StatusCodes.OK)
        responseAs[String] should be("{\"status\":\"ok\",\"totalCalls\":1,\"averageResponseTime\":0.0}")
      }
    }

    "return the result for a health check in XML" in {
      Get("/healthcheck") ~> addHeader("Accept", "application/xml") ~> route ~> check {
        status should be(StatusCodes.OK)
        //TODO: Note this should have a doctype to have valid xml
        responseAs[String] should be("<response>\n    <status> ok </status>\n    <totalCalls> 1 </totalCalls>\n    <avgResponseTime> 0.0 </avgResponseTime>\n</response>")
      }
    }
  }

  /* "Webservice::/findPrice/" should {
    "Return a price for 2015-07-01T07:00:00Z to 2015-07-01T12:00:00Z in JSON" in {
      Get("/findPrice/between/2015-07-01T07:00:00Z/2015-07-01T12:00:00Z") ~> route ~> check {

        status should be(StatusCodes.OK)
        responseAs[String] should be("""{"result": "1500"}"""")
      }
    }

    "Return a price for 2015-07-01T07:00:00Z to 2015-07-01T12:00:00Z in XML" in {
      Get("/findPrice/between/2015-07-01T07:00:00Z/2015-07-01T12:00:00Z") ~> addHeader("Accept", "application/xml") ~> route ~> check {
        status should be(StatusCodes.OK)
        responseAs[String] should be("<result> ok </result>")
      }

    }
    "Return a price for 2015-07-04T07:00:00Z to 2015-07-04T12:00:00Z in JSON" in {
      Get("/findPrice/between/2015-07-04T07:00:00Z/2015-07-04T12:00:00Z") ~> route ~> check {

        status should be(StatusCodes.OK)
        responseAs[String] should be("""{"result": "2000"}"""")
      }
    }
    "Return a price for 2015-07-04T07:00:00Z to 2015-07-04T20:00:00Z in JSON" in {
      Get("/findPrice/between/2015-07-04T07:00:00Z/2015-07-04T20:00:00Z") ~> route ~> check {

        status should be(StatusCodes.OK)
        responseAs[String] should be("""{"result": "unavailable"}"""")
      }
    }

  }*/


}