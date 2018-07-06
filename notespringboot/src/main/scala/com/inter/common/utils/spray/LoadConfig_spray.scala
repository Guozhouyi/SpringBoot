package com.inter.common.utils.spray

import akka.actor.ActorSystem
import spray.client.pipelining.{Get, sendReceive}
import spray.http.{HttpResponse}

import scala.concurrent.Future
import scala.util.Success

object LoadConfig_spray {
  def getConf(): Unit = {
    implicit val loadConfig_spray = ActorSystem("LoadConfig_spray")
    import loadConfig_spray.dispatcher

    val pipline = sendReceive

    val response: Future[HttpResponse] = pipline(Get("http://localhost:8080/properties"))
    response onComplete {
      case Success(sucess) => {
        println("Sucess : " + sucess.entity.data.asString)
      }
    }
  }
}

object LoadConfig_sprayDriver extends App {
  LoadConfig_spray.getConf()
}
