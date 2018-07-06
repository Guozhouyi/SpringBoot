package com.inter.dao

import org.springframework.context.annotation.{Bean, ComponentScan, Configuration, PropertySource}
import org.springframework.beans.factory.annotation.Value


@Configuration
@ComponentScan(basePackages = Array("com.inter.dao"))
@PropertySource(Array("application.properties"))
class Env2 {
  @Value("${environment}")
  var environment: String = _
  /*
  var host: String = _
  var port: String = _
  var hdfsNM1Host: String = _
  var hdfsNM2Host: String = _*/
  @Bean
  def getEnvironment: String = environment
}

object Env2Driver {
  def main(args: Array[String]): Unit = {
    println(new Env2().getEnvironment)
  }
}