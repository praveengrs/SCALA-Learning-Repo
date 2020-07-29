package com.cart.inventory.streamingflow

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Producer
import akka.kafka.{ProducerSettings, Subscriptions}
import akka.stream.scaladsl.{Flow, GraphDSL, RunnableGraph, Sink, Source}
import akka.stream.{ClosedShape, Materializer}
import com.cart.inventory.bo.Cart
import com.google.gson.Gson
import javax.inject.Inject
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.language.postfixOps

//note:independently run uncomment the below code
//object MessagePublisher extends App {

class MessagePublisher @Inject()(system:ActorSystem) (implicit val mat: Materializer) {
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  def publishMsg(cartInfo:Cart, flag:Boolean): Unit ={
    implicit val actorSystem = ActorSystem("SimpleStream")
    // cartInfo Message handler...
    var cartInfoMessage: String = cartInfo.message;
    if (flag) {//it will go there is no error
      cartInfoMessage = "Unable to publish the message to queue, please refer application log for more details..."
    };
    val gson = new Gson();
    //cart info OBJECT succesfully came
    /* configuration needs to be moved to conf folder */
    val bootstrapServers = "localhost:9092"
    val kafkaTopic = "akka_streams_topic"
    val partition = 0
    /* configuration needs to be moved to conf folder */
    val subscription = Subscriptions.assignment(new TopicPartition(kafkaTopic, partition))
    val producerSettings = ProducerSettings(actorSystem, new ByteArraySerializer, new StringSerializer)
      .withBootstrapServers(bootstrapServers)
    val runnableGraph = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._
      val tickSource = Source.tick(0 seconds, 1 seconds, gson.toJson(cartInfo))
      // plainSink publish the records to Kafka topics
      val kafkaSink = Producer.plainSink(producerSettings)
      val printlnSink = Sink.foreach(println)
      val mapToProducerRecord = Flow[String].map(elem => new ProducerRecord[Array[Byte], String](kafkaTopic, elem))
      tickSource  ~> mapToProducerRecord   ~> kafkaSink
      ClosedShape
    })
    val future = Future {
      runnableGraph.run()
    }
    //    runnableGraph.run()
    val result = Await.result(future, 1 second)
    println("Message produced successfully!")
  }
}
