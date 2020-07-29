package com.cart.inventory.controllers

import com.cart.inventory.bo.Cart
import com.cart.inventory.models.OrderItems
import com.cart.inventory.service.StateTransformer
import javax.inject._
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

class OrderController @Inject()(cc: ControllerComponents, stateTransformer: StateTransformer) extends AbstractController(cc) {
  implicit val orderFormat = Json.format[OrderItems]
  implicit val cancelFormat = Json.format[Cart]
  implicit val logger = Logger(this.getClass)


  def placeOrder = Action(parse.json) {
    req => {
      if (req.hasBody) {
          val order = Try(Json.fromJson[OrderItems](req.body).get)
          Try(stateTransformer.processOrder(order.get))
          order match{
            case Success(o) =>
              logger.info("Order Placed")
              Ok("Order Placed")

            case Failure(e) =>
              logger.error("Please give a valid request body " + e.getMessage + "\n\n")
              BadRequest("Please give a valid request body " + e.getMessage + "\n\n")

          }
      }
      Ok

    }
  }

  def cancelOrder = Action(parse.json) {
    req => {
      if (req.hasBody) {
        val cart = Try(Json.fromJson[Cart](req.body).get)
        Try(stateTransformer.process(cart.get))
        cart match{
          case Success(c) =>
            logger.info("Order Cancelled")
            Ok("Order Cancelled")

          case Failure(e:InterruptedException) =>
            logger.error("Exception Caught: " + e.getMessage + "\n\n")
            NotAcceptable("Exception Caught: " + e.getMessage + "\n\n")

        }

      }
      Ok("Posting Cancel Order Details: ")
    }

  }

}