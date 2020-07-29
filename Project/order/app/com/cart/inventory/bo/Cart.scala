package com.cart.inventory.bo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

case class Cart(orderId:Int=0, state:String, items:String, source:String="", message:String="")

//source:String, message:String

@JsonIgnoreProperties(ignoreUnknown=true)
case class CartInfoList(cartInfoList:List[Cart])
