package com.inter.spring_boot.service

import com.inter.spring_boot.properties.Config
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ConfigService @Autowired()(propertie: Config) {
  def getMessage() = s"Message : '${propertie.toString}'"

  def getEnv: String = propertie.environment
}
