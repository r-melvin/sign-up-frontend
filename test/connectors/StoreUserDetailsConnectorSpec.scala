package connectors

import akka.actor.Status.Success
import akka.stream.Materializer
import mockws.{MockWS, Route}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, ControllerComponents, Results}
import utils.TestData._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class StoreUserDetailsConnectorSpec extends PlaySpec with ScalaFutures with Results with MockitoSugar with GuiceOneServerPerSuite {

  implicit lazy val materializer: Materializer = app.materializer
  lazy val controllerComponents: ControllerComponents = app.injector.instanceOf[ControllerComponents]

//  def await[A](future: Future[A]): A = Await.result(future, 5 seconds)

  "storeUserDetails" should {

    "return user's credentials when successful" in {
      val testRoute = Route {
        case ("POST", _) => controllerComponents.actionBuilder(
          Created(Json.toJson(credentials))
        )
      }

      val ws = MockWS(testRoute)
      val connector = new StoreUserDetailsConnector(ws)

      val result = Await.result(connector.storeUserDetails(userData), 5 seconds)

      result mustBe Right(Json.toJson(credentials))
    }
  }
}