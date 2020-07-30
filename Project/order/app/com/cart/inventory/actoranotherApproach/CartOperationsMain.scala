package com.cart.inventory.actoranotherApproach

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import akka.actor.typed.{ActorRef, ActorSystem, Behavior, SpawnProtocol}
import akka.actor.typed.scaladsl.Behaviors

object CartOperationsMain extends App {

  def behavior(status: String): Behavior[CartOperations] = Behaviors.receiveMessage {
    message =>
      println(message.toString)
      message match {
        case UpdateCartStatus(_, status) => status match {
          case "ORDERED" => behavior("ORDERED")
          case "CANCELLED" => behavior("CANCEL") //validate the state change
          case "PACKED" => behavior("PACKED")
          case "SHIPPED" => behavior("SHIPPED")
          case "DELIVERED" => behavior("DELIVERED")
        }
      }
  }

  def behavior(order: Order): Behavior[CartOperations] = Behaviors.setup { context =>

    val orderId = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now).toLong
    val child = context.spawn(CartOperationsMain.behavior("ORDERED"), "order" + orderId)

    println("this behavior is called")
    println(child)
    child ! UpdateCartStatus(1,"ORDERED")
    behavior(order)
  }

  val actorSystem = ActorSystem(SpawnProtocol(), "CartActor")
  val items: Order = Order("Wallet", 1, 20.8)
  val parentActor: ActorRef[CartOperations] = actorSystem.systemActorOf(behavior(items), "Supervisor")

}