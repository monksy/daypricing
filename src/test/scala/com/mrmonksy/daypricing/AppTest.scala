package com.mrmonksy.daypricing

import akka.http.scaladsl.model.{HttpHeader, StatusCode, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{FlatSpec, Matchers, WordSpec}

class AppTest extends WordSpec with Matchers with ScalatestRouteTest with ServiceRoutes  {

  "Webservice::/healthcheck" should {
    "return the result for a health check in JSON (the default)" in {
      Get("/healthcheck") ~> route ~> check {
        status should be (StatusCodes.OK)
        responseAs[String] should be ("{\"status\":\"ok\"}")
      }
    }

    "return the result for a health check in XML" in {
      Get("/healthcheck") ~> addHeader("Accept", "application/xml") ~> route ~> check {
        status should be (StatusCodes.OK)
        //TODO: Note this should have a doctype to have valid xml
        responseAs[String] should be ("<status> ok </status>")
      }
    }
  }

}