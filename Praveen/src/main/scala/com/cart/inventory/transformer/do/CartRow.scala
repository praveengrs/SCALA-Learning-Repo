package com.cart.inventory.transformer.`do`

import com.cart.inventory.const.AppConst.STATE_ORDERED

case class CartRow(orderId:Int=0, state:String=STATE_ORDERED, items:String, source:String, message:String)
