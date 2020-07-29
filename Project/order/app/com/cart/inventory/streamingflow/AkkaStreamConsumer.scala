package com.cart.inventory.streamingflow
import akka.actor.{ActorRefFactory, ActorSystem}
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.scaladsl.Sink
import com.cart.inventory.bo.CartInfoList
import com.cart.inventory.service.StateTransformer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import javax.inject.Inject
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}
import akka.actor.ActorSystem
import akka.kafka.ConsumerSettings
import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import javax.inject.Inject
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

//note:independently run uncomment the below code
//object AkkaStreamConsumer extends App {

class AkkaStreamConsumer @Inject()(system:ActorSystem) (implicit val mat: Materializer) {

  val logger = LoggerFactory.getLogger(getClass().getName());
  implicit val materializer: Materializer.type = Materializer


  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  //create the consumer configs:
  val bootStrapServer: String = "localhost:9092"
  val groupId: String = "StateMachine"
  val consumerTopic: String = "TeamUST"

  val config = system.settings.config.getConfig("akka.kafka.consumer")
  val consumerSettings =
    ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
      .withBootstrapServers(bootStrapServer)
      .withGroupId((groupId))
      .withProperty("delete.topic.enable", "true")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  //subscribing the consumer messages
  //mapper Json object to string
    println("Waiting for the producer to write data")
  val done = Consumer
    .plainSource(consumerSettings, Subscriptions.topics(consumerTopic))
    .runWith(Sink.foreach(s => {
       println(" producer to write data" + s)
      val serverData = mapper.readValue(s.value(), classOf[CartInfoList])
      println(serverData)
     // val record = Consumer.poll(java.time.Duration.ofMillis(5000)).asScala
      serverData.cartInfoList.foreach(dataCartInfo => {
        println(" state machine starts >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + dataCartInfo)
        val stateTransformer= new StateTransformer()
        stateTransformer.process(dataCartInfo)
      })
    })) // just print each message for debugging

  implicit val ec: ExecutionContextExecutor = system.dispatcher

  done onComplete {
    case Success(_) => println("Done");
      system.terminate()
    case Failure(err) => println(err.toString);
      system.terminate()
  }
}







