package com.inter.common.utils.spray.demo.client

import akka.actor.ActorSystem
import spray.client.pipelining._

import scala.util.Success

object Client extends App {
  implicit val client = ActorSystem("client")
  import client.dispatcher

  val pipline = sendReceive

  val response = pipline {
    Post("http://localhost:8080/stuff","123")
  }

  response onComplete {
    case Success(sucess) => {
      println("Sucess : " + sucess.entity.data.asString)
    }
  }
}