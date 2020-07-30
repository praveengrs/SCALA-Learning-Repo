package com.cart.inventory.actoranotherApproach

import akka.actor.typed.ActorRef

sealed trait CartOperations

case class Order(itemName:String,quantity:Int,price:Double) extends CartOperations
case class UpdateCartStatus(orderId:Long,status:String) extends CartOperations
case class GetStatus(orderId:Long,replyTo:ActorRef[Order]) extends CartOperations

