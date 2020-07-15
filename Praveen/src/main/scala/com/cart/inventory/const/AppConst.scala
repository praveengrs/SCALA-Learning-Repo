package com.cart.inventory.const

object AppConst {

  val STATE_ORDERED = "Ordered"
  val STATE_PACKED = "Packed"
  val STATE_CANCELLED= "Cancelled"
  val STATE_SHIPPED= "Shipped"
  val STATE_DELIVERED= "Delivered"

  val STATE_TRANSITION: Map [String, List[String]] = Map(STATE_ORDERED -> List(STATE_PACKED,STATE_CANCELLED),
    STATE_PACKED -> List(STATE_SHIPPED,STATE_CANCELLED),
    STATE_CANCELLED -> List(STATE_ORDERED),
    STATE_SHIPPED -> List(STATE_DELIVERED),
    STATE_DELIVERED -> List()  )


}
