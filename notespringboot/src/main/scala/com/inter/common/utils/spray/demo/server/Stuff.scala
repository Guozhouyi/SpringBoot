package com.inter.common.utils.spray.demo.server

import spray.json.DefaultJsonProtocol

case class Stuff(id: Int, data: String)

object Stuff extends DefaultJsonProtocol {
  implicit val stuffFormat = jsonFormat2(Stuff.apply)
}
