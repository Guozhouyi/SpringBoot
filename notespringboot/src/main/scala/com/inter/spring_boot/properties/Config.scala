package com.inter.spring_boot.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

import scala.beans.BeanProperty

@Component
@ConfigurationProperties
case class Config() {
  @BeanProperty
  var environment: String = _
  @BeanProperty
  var host: String = _
  @BeanProperty
  var port: String = _
  @BeanProperty
  var hdfsNM1Host: String = _
  @BeanProperty
  var hdfsNM2Host: String = _

  override def toString: String = "Properties =>" + environment + ":" + host + ":" + port + ":" + hdfsNM1Host + "," + hdfsNM2Host
}
