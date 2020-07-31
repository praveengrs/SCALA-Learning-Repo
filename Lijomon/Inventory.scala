package walmart.custom

import akka.actor.typed.ActorRef
import walmart.custom.models.OrderItems

sealed trait Inventory

case class CreateOrder(orderItem: OrderItems)  extends Inventory
case class UpdateOrder(orderItem: OrderItems) extends Inventory
case class CancelOrder(orderItem: OrderItems) extends Inventory

case class StopService(replyTo: ActorRef[OrderItems]) extends Inventory
