package com.cart.inventory.bean

import com.fasterxml.jackson.annotation.JsonProperty

@JsonProperty
case class Item(@JsonProperty orderId:Int, @JsonProperty state:String, @JsonProperty items:String, @JsonProperty source:String, @JsonProperty message:String)
