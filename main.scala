import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

import scala.io.StdIn

object Main {
  // Case class for our API response
  case class ApiResponse(message: String)

  // JSON formatting for our ApiResponse
  implicit val apiResponseFormat = jsonFormat1(ApiResponse)
  
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem(Behaviors.empty, "scala-rest-api-generator")
    implicit val executionContext = system.executionContext

    val route =
      path("hello") {
        get {
          complete(ApiResponse("Hello, World!"))
        }
      }

    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
    println("Server online at http://localhost:8080/")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
