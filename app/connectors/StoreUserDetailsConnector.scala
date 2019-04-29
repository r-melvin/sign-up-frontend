package connectors

import play.api.http.Status._
import models.{Credentials, UserData}
import play.api.Configuration
import play.api.libs.json.{JsValue, Json}
import javax.inject.Inject

import scala.concurrent.Future
import scala.concurrent.duration._
import play.api.mvc._
import play.api.libs.ws._
import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.util.ByteString
import java.util.UUID.randomUUID

import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.ws.WSClient

import scala.util.Random

class StoreUserDetailsConnector @ Inject()(ws: WSClient)(implicit ec: ExecutionContext) {
  def storeUserDetails(details: UserData): Future[Either[String, JsValue]] = {
    val request: WSRequest = ws.url("http://localhost:9001/")
    request
      .addHttpHeaders("Content-Type" -> "application/json")
      .post(Json.toJson(details))
      .map(response =>
        response.status match {
          case CREATED =>
            Right(Json.toJson(Credentials(details.email, details.password)))
          case INTERNAL_SERVER_ERROR =>
            Left("Gutted")
        }
      )

  }
}