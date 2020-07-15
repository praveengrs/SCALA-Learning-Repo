package com.cart.inventory.transformer

import com.cart.inventory.bo.CartInfo
import com.cart.inventory.const.AppConst._
import com.cart.inventory.transformer.`do`.CartRow
import com.cart.inventory.transformer.dao.CartDAO

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.util.{Success, Try}

class StateTransformer {


      def process(cartInfo: CartInfo) ={

            updateCart(cartInfo).map(_=>{
                  producer(cartInfo,_)
            }).recover({
                  case ex => producer(cartInfo,false)
            })
      }

      def producer(cartInfo:CartInfo, flag:Boolean) ={

            println(flag)
      }

      def updateCart(cartInfo: CartInfo): Try[(Boolean)] = {

            (cartInfo state , cartInfo orderId) match {
                  case (STATE_ORDERED ,0)=> {

                       val insertStatus =  Await.result((CartDAO persistCart(CartRow(items=cartInfo.items,source=cartInfo.source,message=cartInfo.message)) ),60 second)
                         Success(   insertStatus  == 1 )
                  }

                  case _ => {

                          val currentOrder = Await.result(CartDAO.fetchCartStatus(cartInfo orderId), 60 second)

                        currentOrder match {

                              case x if(x.size == 0) => throw new InstantiationException("Order Id not existing")
                              case _ => {

                                    val validStateTransition = STATE_TRANSITION getOrElse(currentOrder.head.state, (List())) contains(cartInfo state)
                                    if (validStateTransition){

                                          val updateStatus =  Await.result((CartDAO persistCart(CartRow(cartInfo.orderId,cartInfo.state,cartInfo.items,cartInfo.source,cartInfo.message))) ,60 second)
                                          Success(updateStatus == 1)
                                    }

                                    else throw new InterruptedException("Invalid transition of state")
                              }
                        }


                  }

            }



      }

}
