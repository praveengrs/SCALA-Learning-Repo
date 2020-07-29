package controllers

import com.cart.inventory.controllers.OrderController
import com.cart.inventory.service.StateTransformer
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Results
import play.api.test._
import play.api.test.Helpers._

class OrderControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting{

  "OrderController GET" should {

    "create order" in {

      val jsonRequest: String = """{"items":"mobile"}"""
      val controller = new OrderController(stubControllerComponents(), new StateTransformer)
      val result = controller.placeOrder().apply(FakeRequest(POST, "/placeOrder")
        .withHeaders("Content-type" -> "application/json")
        .withBody[JsValue](Json.parse(jsonRequest)))
      status(result) mustBe OK
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Order Placed")

    }

    "cancel order" in {
      val controller = new OrderController(stubControllerComponents(), new StateTransformer)
      val result1 = controller.cancelOrder().apply(FakeRequest(PUT, "/cancelOrder")
        .withHeaders("Content-type" -> "application/json")
        .withBody(Json.parse("{\"orderId\":8,\"state\":\"Cancelled\",\"items\":\"mobile\",\"source\":\"\",\"message\":\"\"}")))
      status(result1) mustBe OK
      contentType(result1) mustBe Some("text/plain")
      contentAsString(result1) must include("Posting Cancel Order Details: ")

    }

    "render the put page exception from the router" in {
      val request = FakeRequest(PUT, "/cancelOrder")
        .withJsonBody(Json.parse("{\"orderId\":4,\"state\":\"Cancelled\",\"items\":\"television\",\"source\":\"\",\"message\":\"\"}"))
      val home = route(app, request).get

      status(home) mustBe NOT_ACCEPTABLE
      contentType(home) mustBe Some("text/plain")
      contentAsString(home) must include ("Invalid transition of state")
    }



//    "render the placeOrder page from the router" in {
//      val request = FakeRequest(POST, "/placeOrder").withJsonBody(Json.parse("{\"items\":\"smartphone\"}"))
//      val home = route(app, request).get
//
//      status(home) mustBe OK
//      contentType(home) mustBe Some("text/plain")
//      contentAsString(home) must include ("Order Placed")
//    }

  }
}