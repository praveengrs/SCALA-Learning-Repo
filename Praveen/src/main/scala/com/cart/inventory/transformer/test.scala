package com.cart.inventory.transformer

import com.cart.inventory.bo.CartInfo
import com.cart.inventory.const.AppConst
import com.cart.inventory.transformer.`do`.CartRow
import com.cart.inventory.transformer.dao.CartDAO

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object test extends App {

  println("Hello world")

  //Await.result(dao.getAllCart(), 60 second).foreach(println(_))

  //println( Await.result(dao.insertCart(CartRow(orderId=None,state="Order",items="1test4123",source="WS4",message="NA")), 60 second) )

  //println( Await.result(dao.createEmployee(CartRow(orderId=None,state="Order",items="1test4123",source="WS4",message="NA")), 60 second) )

  //println( Await.result(dao.persistCart(), 60 second) )




  //println( Await.result(CartDAO.fetchCartStatus(300), 60 second) )

  val st = new StateTransformer
  st.updateCart(CartInfo(orderId=8,state=AppConst.STATE_SHIPPED,items="Jaya",source="Ac",message="NA"))



}
