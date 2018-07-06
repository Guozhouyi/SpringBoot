package com.inter.common

import java.io.{InputStreamReader, Reader}
import java.lang.reflect.Type

import com.google.gson.{Gson, GsonBuilder}

import com.inter.dao._

object LoadJson {
  def getConfiuration[T](json: String, dao: Class[T]): T = {
    val reader: Reader = new InputStreamReader(this.getClass.getResourceAsStream(json), "UTF-8")
    val gson: Gson = new GsonBuilder().create()
    gson.fromJson(reader, dao)
  }
  def getConfig(): Env ={
    LoadJson.getConfiuration("/application.json", classOf[Env])
  }
}

object LoadJsonDriver {
  def main(args: Array[String]): Unit = {
    println(LoadJson.getConfig.environment)
  }
}
