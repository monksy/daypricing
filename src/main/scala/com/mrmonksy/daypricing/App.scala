package com.mrmonksy.daypricing


import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.MediaTypeNegotiator
import akka.stream.ActorMaterializer
import com.mrmonksy.daypricing.App.{getClass, system}
import org.joda.time
import spray.json.{DefaultJsonProtocol, _}

import scala.util.Try

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val healthFormat = jsonFormat3(HealthResponse)
  implicit val priceFormat = jsonFormat1(PriceResponse)
}

/**
  * This is a quirk of akka-http. It has to be a trait to be able to isolate it's self from the server standup.
  */
trait ServiceRoutes {
  val confAssistant = new ConfigurationAssistant(None)
  val configValues = Try(confAssistant.grabConfigurationAndConvert())

  //Lets log everything out if there was a configuration issue, note we dont' have a logger here because it's a trait
  configValues.failed.map(ex => {
    System.err.println(confAssistant.makeNiceErrorMessage(ex))
    ex.printStackTrace()
    System.exit(1)
  })

  val dataManager = new PriceDataManager(configValues.get)


  //FIXME: Record keeping (could be better), we're going to lie aobut the number of calls to avoid a divide by zero
  var numberOfCalls = 1L
  var allTimeSpent = 0L


  val route =
    pathPrefix("findPrice" / "between" / Segment) { fromDate => {
      validate(Try(time.DateTime.parse(fromDate)).isSuccess, s"Error: fromDate of $fromDate isn't valid. Please use a date in the format similar to: 2015-07-01T12:00:00Z ") {
        path(Segment) { toDate => {
          validate(Try(time.DateTime.parse(toDate)).isSuccess, s"Error: endDate of $toDate isn't valid. Please use a date in the format similar to: 2015-07-01T12:00:00Z ") {
            (get & extract(_.request.headers)) { headers =>
              complete {
                numberOfCalls += 1
                val startTime = System.currentTimeMillis()

                val mediaNegotiator = new MediaTypeNegotiator(headers)
                val fullFrom = time.DateTime.parse(fromDate)
                val fullEnd = time.DateTime.parse(toDate)

                val result = dataManager.findPriceBetween(fullFrom, fullEnd)
                val smallResponse = PriceResponse(result.map(_.price).getOrElse("unavailable").toString)

                allTimeSpent += (System.currentTimeMillis() - startTime)

                HttpResponse(entity = convertPriceResponse(smallResponse, mediaNegotiator))
              }
            }
          }
        }
        }
      }
    }
    } ~
      path("healthcheck") {
        (get & extract(_.request.headers)) {
          headers =>
            complete {
              val mediaNegotiator = new MediaTypeNegotiator(headers)
              val result = HealthResponse("ok", numberOfCalls, allTimeSpent / numberOfCalls)
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
        <response>
          <status>
            {health.status}
          </status> <totalCalls>
          {health.totalCalls}
        </totalCalls> <avgResponseTime>
          {health.averageResponseTime}
        </avgResponseTime>
        </response>

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
  val logger = Logging(system, getClass)

  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher


  logger.info("Starting the service at http://localhost:8080")
  val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 8080)


}


final case class HealthResponse(status: String, totalCalls: Long, averageResponseTime: Double)

final case class PriceResponse(result: String)