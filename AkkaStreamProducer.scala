package com.cart.inventory.flows.raheem

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import com.cart.inventory.bean.Item
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object AkkaStreamProducer extends App {

      var itemsList: List[Item] = List(Item(1001, "KL", "OIL", "UST Source", "Thanks 1"),
                                       Item(1002, "TN", "TOIL", "UST TSource", "T - Thanks 2"),
        Item(1003, "MN", "MOIL", "UST MSource", "M - Thanks 3"),
        Item(1004, "NN", "NOIL", "UST NSource", "N - Thanks 4"))

      implicit val system: ActorSystem = ActorSystem("producer-sample")
      implicit val materializer: Materializer  = Materializer(system)

      val bootStrapServer: String = "localhost:9092"
      val groupId: String = "My-First-Application"
      val producerTopic: String = "mytopic"

      //create the consumer configs:
      val producerSettings =
        ProducerSettings(system, new StringSerializer, new StringSerializer)
          .withBootstrapServers(bootStrapServer)

      val done: Future[Done] =
        Source(1 to 3)
          .map(value => new ProducerRecord[String, String](producerTopic, acceptItem()))
          .runWith(Producer.plainSink(producerSettings))

      implicit val ec: ExecutionContextExecutor = system.dispatcher
      done onComplete  {
        case Success(_) => println("Done"); system.terminate()
        case Failure(err) => println(err.toString); system.terminate()
      }

  def acceptItem(): String = {
    var itemsList:List[Item] = List(Item(1001, "KL", "OIL", "UST Source", "Thanks 1"),
      Item(1002,"TN", "TOIL", "UST TSource", "T - Thanks 2"),
      Item(1003,"MN", "MOIL", "UST MSource", "M - Thanks 3"),
      Item(1004,"NN", "NOIL", "UST NSource", "N - Thanks 4"))

    itemsList.toString
  }
}

