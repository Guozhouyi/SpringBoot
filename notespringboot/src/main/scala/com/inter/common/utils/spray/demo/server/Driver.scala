package com.inter.common.utils.spray.demo.server

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http

object Driver extends App {
  implicit val system = ActorSystem("spray-server")

  val service = system.actorOf(Props[Server], "spray-server")

  //If we're on cloud foundry, get's the host/port from the env vars
  lazy val host = Option(System.getenv("VCAP_APP_HOST")).getOrElse("localhost")
  lazy val port = Option(System.getenv("VCAP_APP_PORT")).getOrElse("8080").toInt
  IO(Http) ! Http.Bind(service, host, port = port)
}