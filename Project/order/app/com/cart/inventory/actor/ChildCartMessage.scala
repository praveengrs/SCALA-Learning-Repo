package com.cart.inventory.cart

import akka.actor.typed.ActorRef

/*sealed trait BankAccountMessage3

case class Deposit3(amount:Int) extends BankAccountMessage3
case class Withdraw3(amount:Int) extends BankAccountMessage3
case object PrintBalance3 extends BankAccountMessage3
case class GetBalance3(replyTo:ActorRef[Int]) extends BankAccountMessage3*/

//case class Cart4(orderId:Int , state:String, Items:String, source:String="",message:String="")

//item: String, state: String, source: String, message :String
sealed trait ChildCartMessage
//case object simpleAdd extends CartMessage
case class AddUserOrder(parentreplyTo:ActorRef[CartMessage3],cart: Cart3) extends ChildCartMessage
case class CancelUserOrder(parentReplyTo:ActorRef[CartMessage3],cart:Cart3) extends ChildCartMessage
case object PrintUserOrder extends ChildCartMessage
case class GetUserOrder(replyTo:ActorRef[ChildCartMessage]) extends ChildCartMessage