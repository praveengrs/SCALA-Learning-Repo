//package com.cart.inventory.service
//
//import com.cart.inventory.bo.Cart
//import com.cart.inventory.models.OrderItems
//import com.cart.inventory.service.StateTransformer
//import javax.inject.Inject
//import com.cart.inventory.const.AppConst.STATE_ORDERED
//import play.api.Logger
//
////import scala.concurrent.Future
//
//class OrderService @Inject()(stateTransformer: StateTransformer) {
//  implicit val logger = Logger(this.getClass)
//
//  def processOrder(cartItems: OrderItems) = {
//    val order:Cart= Cart(state = STATE_ORDERED, items = cartItems.items)
//    //TODO : validation of order id
//    // StateTransformer => OrderServiceUtil
//    //orderServiceUtil.validateOrderId()
//    //orderServiceUtil.validateAvailableCountOfItems()
//   //dao.save()
//    stateTransformer.process(order)
//    //logger.info("")
//  }
//
//  def cancelOrder(cartInfo: Cart): Unit ={
//    stateTransformer.process(cartInfo)
//  }
//
//
//}
