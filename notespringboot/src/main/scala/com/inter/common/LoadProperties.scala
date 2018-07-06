package com.inter.common

import org.apache.commons.configuration2.Configuration
import org.apache.commons.configuration2.builder.fluent.Configurations

object LoadProperties {
  @throws(classOf[Exception])
  def getConfiguration(properties: String): Configuration = {
    val configuration = new Configurations()
    configuration.properties(properties)
  }
}

object ConfigDriver{
  def main(args: Array[String]): Unit = {
    println(LoadProperties.getConfiguration("application.properties").getString("environment"))
  }
}