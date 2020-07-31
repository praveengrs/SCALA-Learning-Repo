package walmart.custom

import akka.actor.typed.SpawnProtocol.Spawn
import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed._
import akka.util.Timeout
import walmart.custom.models.OrderItems

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object Main extends App {

  // Item will get from service
  val cartInfo:OrderItems = new OrderItems(101, "CA", "Computer", "Warehouse1", "CreateOrder" )

  def behavior(orderItem: OrderItems): Behavior[Inventory] = Behaviors.receiveMessage {
    case CreateOrder(orderItem)  => processOrder(orderItem)
      behavior(orderItem)
    case UpdateOrder(orderItem)  => upcateOrder(orderItem)
      behavior(orderItem)
    case CancelOrder(orderItem) => cancelOrder(orderItem)
      behavior(orderItem)
    case StopService(replyTo) => replyTo ! orderItem
      Behaviors.same
  }

  val actorSystem = ActorSystem(SpawnProtocol(), name = "InventorySystem")

  implicit val timeout: Timeout     = Timeout(1.seconds)
  implicit val scheduler: Scheduler = actorSystem.scheduler
  import actorSystem.executionContext

  val inventoryFuture: Future[ActorRef[Inventory]] =
    actorSystem.ask[ActorRef[Inventory]] { ref =>
      Spawn[Inventory](
        behavior = behavior(cartInfo),
        name = "actor1",
        props = Props.empty,
        replyTo = ref
      )
    }

  // future for making new DB entry
  val createFuture = for {
    itemStore <- inventoryFuture
    stkBbalance    <- itemStore ? StopService
    _           = println(stkBbalance + " StartService")
    _           = itemStore ! CreateOrder(cartInfo)
    stkBbalance2    <- itemStore ? StopService
    _           = println(stkBbalance2 + " StopService")
  } yield ()

  def processOrder(orderItem: OrderItems) = {
    // process(orderItem) - StateTransformer process method will be called.
    println("Processing Order for new entry...")
  }

  // future for updating DB entry
  val updateFuture = for {
    itemStore <- inventoryFuture
    stkBbalance    <- itemStore ? StopService
    _           = println(stkBbalance   + " StartService")
    _           = itemStore ! UpdateOrder(cartInfo)
    stkBbalance2    <- itemStore ? StopService
    _           = println(stkBbalance2 + " StopService")
  } yield ()

  def upcateOrder(orderItem: OrderItems) = {
    println("Updating the order...")
  }

  // future for cancelling DB entry
  val cancelFuture = for {
    itemStore <- inventoryFuture
    stkBbalance    <- itemStore ? StopService
    _           = println(stkBbalance  + " StartService")
    _           = itemStore ! CancelOrder(cartInfo)
    stkBbalance2    <- itemStore ? StopService
    _           = println(stkBbalance2 + " StopService")
  } yield ()

  def cancelOrder(orderItem: OrderItems) = {
    println("Cancelling the order...")
  }

  Await.result(createFuture, 2.seconds)
  Await.result(updateFuture, 2.seconds)
  Await.result(cancelFuture, 2.seconds)

  // ActorSystem needs a terminate to close after process...
  actorSystem.terminate()
}