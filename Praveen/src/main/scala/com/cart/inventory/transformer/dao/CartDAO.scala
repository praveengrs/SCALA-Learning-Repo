package com.cart.inventory.transformer.dao

import com.cart.inventory.transformer.`do`.{CartRow, CartTable}
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.duration._
import scala.language.postfixOps

import scala.concurrent.{Await, Future}

object CartDAO {

  var driverName:String = "com.mysql.cj.jdbc.Driver"
  var url:String = "jdbc:mysql://localhost:3306/test?user=root&password=Me@Tech1998"
  val db = Database.forURL(url, driver = driverName )

  // creating instance of mapping class
  private val carts = TableQuery[CartTable]

  def persistCart(cart:CartRow)  =  db.run(carts.insertOrUpdate(cart))

  def fetchCartStatus(orderIdId:Int) = db.run(carts.filter(e=>e.orderId === orderIdId).result)





}


