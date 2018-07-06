package com.inter.common

import java.util.Properties

object LoadProperties2 {
  def loadProperties(properties: String): Properties = {
    val inputStream = this.getClass.getResourceAsStream(properties)
    val prop: Properties = new Properties()
    prop.load(inputStream)
    prop
  }
}

object LoadProperties2Driver {
  def main(args: Array[String]): Unit = {
    println(LoadProperties2.loadProperties("/application.properties").getProperty("environment"))
  }
}
