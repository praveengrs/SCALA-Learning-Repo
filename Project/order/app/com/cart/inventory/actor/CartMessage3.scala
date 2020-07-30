package com.cart.inventory.actor

import akka.actor.typed.ActorRef

/*sealed trait BankAccountMessage3

case class Deposit3(amount:Int) extends BankAccountMessage3
case class Withdraw3(amount:Int) extends BankAccountMessage3
case object PrintBalance3 extends BankAccountMessage3
case class GetBalance3(replyTo:ActorRef[Int]) extends BankAccountMessage3*/

case class Cart3(orderId:Int , state:String, Items:String, source:String="",message:String="")

//item: String, state: String, source: String, message :String
sealed trait CartMessage3
//case object simpleAdd extends CartMessage
case class AddOrder3(parentreplyTo: ActorRef[CartMessage3], cart: Cart3) extends CartMessage3
case class CancelOrder3(parentreplyTo: ActorRef[CartMessage3],cart:Cart3) extends CartMessage3
case object PrintCart3 extends CartMessage3
case class GetCart3(replyTo:ActorRef[CartMessage3]) extends CartMessage3
case class Response3(msg: String) extends CartMessage3