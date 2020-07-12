package com.cart.inventory

import akka.actor.ActorSystem
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.scaladsl.{Keep, Sink}
import akka.stream.{ActorMaterializer, Materializer}
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object AkkaStreamConsumer extends App {

  val logger = LoggerFactory.getLogger(getClass().getName());
    implicit val system: ActorSystem = ActorSystem("consumer-sample")
    implicit val materializer: Materializer = Materializer(system)

    //create the consumer configs:
    val bootStrapServer: String = "localhost:9092"
    val groupId: String = "StateMachine"
    val consumerTopic: String = "mytopic"

    val consumerSettings =
      ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
        .withBootstrapServers(bootStrapServer)
        .withGroupId((groupId))
        .withProperty("delete.topic.enable", "true")

    //subscribing the producer messages
    val done = Consumer
      .plainSource(consumerSettings, Subscriptions.topics(consumerTopic))
      .runWith(Sink.foreach(println)) // just print each message for debugging

    implicit val ec: ExecutionContextExecutor = system.dispatcher
    done onComplete {
      case Success(_) => println("Done"); system.terminate()
      case Failure(err) => println(err.toString); system.terminate()
    }
  }


