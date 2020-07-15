package com.cart.inventory.transformer.`do`

import com.cart.inventory.bo.CartInfo
import slick.lifted.Tag
import slick.jdbc.MySQLProfile.api._

class CartTable(tag: Tag) extends Table[CartRow](tag, "cart") {


  def orderId = column[Int]("orderId", O.PrimaryKey, O.AutoInc)

  def state = column[String]("state")
  def items = column[String]("items")
  def source = column[String]("source")
  def message = column[String]("message")

   def * = (orderId, state, items, source,message)<> ((CartRow.apply _).tupled, CartRow.unapply)
}
