package com.mrmonksy.daypricing


import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.MediaTypeNegotiator
import akka.stream.ActorMaterializer
import spray.json.{DefaultJsonProtocol, _}

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val healthFormat = jsonFormat1(HealthResponse)
}

/**
  * This is a quirk of akka-http. It has to be a trait to be able to isolate it's self from the server standup.
  */
trait ServiceRoutes {
  val route =
    path("healthcheck") {
      (get & extract(_.request.headers)) { headers =>
        val mediaNegotiator = new MediaTypeNegotiator(headers)
        val result = HealthResponse("ok")
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
  private def convertHealthResultToResponse(health: HealthResponse, mediaTypeNegotiator: MediaTypeNegotiator) = {
    var response = ""
    var contentType: ContentType.NonBinary = ContentTypes.`application/json`
    if (mediaTypeNegotiator.isAccepted(MediaTypes.`application/json`)) {
      import MyJsonProtocol._

      response = health.toJson.toString()

    } else {
      val pureXml =
        <status>
          {health.status}
        </status>

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
  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)




}


final case class HealthResponse(status: String)