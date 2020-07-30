package com.cart.inventory.actor

import akka.actor.typed.SpawnProtocol.Spawn
import akka.actor.typed._
import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.scaladsl.Behaviors
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
/*
import com.cart.inventory.CartActor3.{order}
import com.cart.inventory.Mainask.{ actorSystemask, behavior}
*/
import scala.collection.mutable.Map
object CartActor3 extends App {

//var carts: ListBuffer[Cart] = ListBuffer()
val childCartMessagesMap: Map[Int,ActorRef[ChildCartMessage]]=Map()

  //items: String, state: String, source: String, message :String
//def childBehavior(carts: ListBuffer[Cart3]=ListBuffer()):Behavior[ChildCartMessage]  = Behaviors.receiveMessage[ChildCartMessage] {
def childBehavior(cartChild: Cart3):Behavior[ChildCartMessage]  = Behaviors.receiveMessage[ChildCartMessage] {
  case AddUserOrder(parentReplyTo,cart) =>
    println(s"Child Actor Add= ${cart.orderId} ${cart.Items},${cart.state}, replyTo = $parentReplyTo")
    parentReplyTo ! Response3("Order Added")
    println("Completed Child")
    childBehavior(cart)
    //Behaviors.same
  case CancelUserOrder(parentReplyTo,cart) =>
    println(s"child actor cancel= ${cart.orderId} ${cart.Items},${cart.state} ,replyTo = $parentReplyTo")
    parentReplyTo ! Response3("Order Cancelled")
    println("Completed Child")
    childBehavior(cart)
    //Behaviors.same
/*  case GetUserOrder(replyTo) =>
    replyTo ! carts
    println(s"Current cart $carts")
    //behavior(balance)
    Behaviors.same*/
}


// def behavior(carts: ListBuffer[Cart3]=ListBuffer()):Behavior[CartMessage3]  = Behaviors.receiveMessage[CartMessage3] {
def behavior(childCartMessagesMap: Map[Int,ActorRef[ChildCartMessage]]):Behavior[CartMessage3]  = Behaviors.receiveMessage[CartMessage3] {
    case AddOrder3(parentreplyTo,cart) =>
      //child1 spawn
      //child1 ? add(cart, reply)
      //childrefmap -> orderid, childRef

      println(s"Spawn child Actor to add order = $cart")
     implicit val timeout: Timeout     = Timeout(2.seconds)
      implicit val scheduler: Scheduler = cartActorSystem.scheduler
      import cartActorSystem.executionContext
      val childActorFuture:Future[ActorRef[ChildCartMessage]]=
      cartActorSystem.ask[ActorRef[ChildCartMessage]] {
        ref => Spawn[ChildCartMessage](
          behavior = childBehavior(cart),
          name="Order",
          props = Props.empty,
          replyTo = ref)}
      val childmap: Map[Int,ActorRef[ChildCartMessage]] =Map()
      val futureChildCart = for {
        currentchildCart <- childActorFuture
        childMap = Map(cart.orderId -> currentchildCart) ++ childCartMessagesMap
        _ = currentchildCart ! AddUserOrder(parentreplyTo,cart)
        _ = Thread.sleep(2000)
        _ = println (s"currentchildCart")
        _ = println(s"${childMap.keys.foreach{ i =>
          print( "Key = " + i )
          println(" Value = " + childMap(i) )}} ")
      } yield

      Thread.sleep(5000)
      println(s"futureChildCart $futureChildCart")

      //behavior(carts)
      behavior(childCartMessagesMap)
      //Behaviors.same
    case CancelOrder3(parentReplyTo,cart) =>
      Thread.sleep(7000)
      println(s"To Cancel Order ")
      println(s"child actor cancel detail, ${cart.orderId} ${cart.Items},${cart.state} ,replyTo = $parentReplyTo")
      val currentchildCart = childCartMessagesMap(cart.orderId)
      println(s"child actor current, ${cart.orderId} ${cart.Items},${cart.state} ,replyTo = $parentReplyTo")
      implicit val timeout: Timeout     = Timeout(2.seconds)
      implicit val scheduler: Scheduler = cartActorSystem.scheduler
      //currentchildCart ? CancelUserOrder(cart)
      val childCanRep = currentchildCart.ask[CartMessage3](actorref => CancelUserOrder(parentReplyTo,cart))

      //println(s"futureChildCart $futureChildCart")

      /*   var cancelOrderFilter =carts.filter( x => x.orderId == cart.orderId)
       carts.filter( x => x
       .orderId == cart.orderId)
       val index=carts.indexOf(carts.filter( x => x.orderId == cart.orderId)(0) )
       //val index=carts.indexOf(carts.filter( x => x.orderId == cart.orderId))
       println(cancelOrderFilter(0).copy(state="Cancelled"))
        val cancel=cancelOrderFilter(0).copy(state="Cancelled")

         val validStateTransition = STATE_TRANSITION getOrElse(carts.head.state, (List())) contains(cart state)
         if (validStateTransition){}

         println(s"Filtered List $cancelOrderFilter,  index : $index, cart : $cancel")
         carts.update(index,cancel)
         //match cancelOrderFilter.sta
       //carts -= cart
       println(s"Updated carts : $carts")
         behavior(carts)*/
      Behaviors.same
    case PrintCart3 =>
        println(s"Printing ")
        Behaviors.same
    case GetCart3(replyTo) =>
      //replyTo ! carts
      println(s"Current cart ")
      //behavior(balance)
      Behaviors.same
    case Response3(msg) =>
      println(s"Response message $msg")
      Behaviors.same
  }


  println("Hello Cart")
  //val cartActorSystem = ActorSystem(Behaviors.empty,name="StateMachineCartSystem") //system level actor
  //val cartSystemActorRef = cartActorSystem.systemActorOf(behavior(ListBuffer()),"SystemActor")
  //val actorSystemspawn = ActorSystem(SpawnProtocol(),name="MyBankActorSystemSpawn2")
  val cartActorSystem = ActorSystem(SpawnProtocol(), name="StateMachineCartSystem")


  implicit val timeout: Timeout     = Timeout(2.seconds)
  implicit val scheduler: Scheduler = cartActorSystem.scheduler
  import cartActorSystem.executionContext
/*
  val account2 = actorSystemspawn.systemActorOf(behavior(0),"account2")
  println(s"account2 = $account2")
 // account2 ! PrintBalance
  account2 ! Deposit2(101)
  //account2 ! PrintBalance
  account2 ! Withdraw2(50)
  //account2 ! PrintBalance
*/


 val cartActorSystemRef:Future[ActorRef[CartMessage3]]=
   cartActorSystem.ask[ActorRef[CartMessage3]] {
  ref => Spawn[CartMessage3](
    behavior = behavior(Map()),
    name="cartActor3",
    props = Props.empty,
    replyTo = ref)}



  val order1:Cart3= Cart3(orderId=1, state = "Ordered", Items = "Mobile")
  val order2:Cart3= Cart3(orderId=2, state = "Ordered", Items = "TV")
  val order3:Cart3= Cart3(orderId=2, state="Cancelled",Items="",source="Chrome",message="Cancel from Order")

  println("Hello futureCart")

  val futureCart = for {
    currentCart3 <- cartActorSystemRef
    _        = println(s"test")
    _       = currentCart3 ! AddOrder3(currentCart3,order1)
    _       = currentCart3 ! PrintCart3
    _       = currentCart3 ! AddOrder3(currentCart3,order2)
    _       = currentCart3 ! PrintCart3
/*    _       = currentCart3 ? CancelOrder3()
    _       = currentCart3 ! PrintCart3
   cart2 <- currentCart3 ? GetCart3*/
  } yield ()
  val futureCancelCart = for {
    currentCart3 <- cartActorSystemRef
    _ = println(s"test")
    _ = currentCart3 ! PrintCart3
    canrep <-  currentCart3.ask[CartMessage3](actorref => CancelOrder3(currentCart3,order3))
    //CancelOrder3() currentCart3 ?[ActorRef[CartMessage3]]
  } yield()
  cartActorSystem.terminate()

}