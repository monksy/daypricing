package com.mrmonksy.daypricing


import java.io.File

import akka.actor.ActorSystem
import akka.actor.FSM.LogEntry
import akka.event.Logging
import akka.http.javadsl.server.RouteResult
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.MediaTypeNegotiator
import akka.http.scaladsl.server.RouteResult.Rejected
import akka.http.scaladsl.server.directives.DebuggingDirectives
import akka.stream.ActorMaterializer
import com.mrmonksy.daypricing.config.RatesCollection
import com.mrmonksy.daypricing.datamodifiers.PriceDataUtility
import com.typesafe.config.ConfigFactory
import org.joda.time.format.ISODateTimeFormat
import spray.json.{DefaultJsonProtocol, _}

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val healthFormat = jsonFormat3(HealthResponse)
  implicit val priceFormat = jsonFormat1(PriceResponse)
}

/**
  * This is a quirk of akka-http. It has to be a trait to be able to isolate it's self from the server standup.
  */
trait ServiceRoutes {
  //ConfigFacotry defaults to application.json, however, I changed this because I had some issues when building the fat jar and it was overwritting the reference.conf.
  // - Given time I would revert this change
  lazy val fixedRates = RatesCollection(ConfigFactory.parseFile(new File(System.getProperty("config"))))
  lazy val normalizedPrices = fixedRates.rates.flatMap(PriceDataUtility.from)

  lazy val dataManager = new PriceDataManager(normalizedPrices)


  //FIXME: Record keeping (could be better), we're going to lie aobut the number of calls to avoid a divide by zero
  var numberOfCalls = 1L
  var allTimeSpent = 0L

  val route =
    path("findPrice/between") {
      path(Segment / Segment) { case (fromDate, toDate) => {
        (get & extract(_.request.headers)) { headers =>


          complete {
            numberOfCalls += 1
            val startTime = System.currentTimeMillis()

            val mediaNegotiator = new MediaTypeNegotiator(headers)
            val fullFrom = ISODateTimeFormat.dateTime().parseDateTime(fromDate)
            val fullEnd = ISODateTimeFormat.dateTime().parseDateTime(toDate)

            val result = dataManager.findPriceBetween(fullFrom, fullEnd)
            val smallResponse = PriceResponse(result.map(_.price).getOrElse("unavailable").toString)

            allTimeSpent += (System.currentTimeMillis() - startTime)

            HttpResponse(entity = convertPriceResponse(smallResponse, mediaNegotiator))
          }
        }
      }
      }
    } ~
      path("healthcheck") {
        (get & extract(_.request.headers)) { headers =>
          val mediaNegotiator = new MediaTypeNegotiator(headers)
          val result = HealthResponse("ok", numberOfCalls, allTimeSpent / numberOfCalls)
          complete {
            HttpResponse(entity = convertHealthResultToResponse(result, mediaNegotiator))
          }
        }
      }


  /**
    * Converts the health result to a response, based on it's content type. Yes this is terribly ugly. But I'm new to akka-http and could be fixed in another iteration.
    * This determines what meida types are accpetable from the client, and then generates the content based on it.
    *
    * @param health              The health response.
    * @param mediaTypeNegotiator A media negotiator that determines if a content type is acceptable.
    * @return Returns a route with a result.
    */
  def convertHealthResultToResponse(health: HealthResponse, mediaTypeNegotiator: MediaTypeNegotiator) = {
    var response = ""
    var contentType: ContentType.NonBinary = ContentTypes.`application/json`
    if (mediaTypeNegotiator.isAccepted(MediaTypes.`application/json`)) {
      import MyJsonProtocol._

      response = health.toJson.toString()

    } else {
      val pureXml =
        <response><status>
          {health.status}
        </status> <totalCalls>
          {health.totalCalls}
        </totalCalls> <avgResponseTime>
          {health.averageResponseTime}
        </avgResponseTime></response>

      val p = new scala.xml.PrettyPrinter(80, 4)
      contentType = ContentTypes.`text/xml(UTF-8)`
      response = p.format(pureXml)
      pureXml


    }

    HttpEntity(contentType, response)
  }

  def convertPriceResponse(price: PriceResponse, mediaTypeNegotiator: MediaTypeNegotiator) = {
    var response = ""
    var contentType: ContentType.NonBinary = ContentTypes.`application/json`
    if (mediaTypeNegotiator.isAccepted(MediaTypes.`application/json`)) {
      import MyJsonProtocol._
      response = price.toJson.toString()

    } else {
      val pureXml =
        <result>
          {price.result}
        </result>

      val p = new scala.xml.PrettyPrinter(80, 4)
      contentType = ContentTypes.`text/xml(UTF-8)`
      response = p.format(pureXml)
      pureXml


    }

    HttpEntity(contentType, response)
  }


}

/**
  * @author ${user.name}
  */
object App extends App with ServiceRoutes {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  val logger = Logging(system, getClass)


  logger.info("Starting the service at http://localhost:8080")
  val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 8080)


}


final case class HealthResponse(status: String, totalCalls: Long, averageResponseTime: Double)

final case class PriceResponse(result: String)