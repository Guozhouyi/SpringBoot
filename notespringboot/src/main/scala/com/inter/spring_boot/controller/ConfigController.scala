package com.inter.spring_boot.controller

import com.inter.spring_boot.service.ConfigService
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RestController}

@RestController
class ConfigController @Autowired()(configService: ConfigService) {
  private val logger = LoggerFactory.getLogger(classOf[ConfigController])

  @ApiOperation("properties scala 测试！！")
  @RequestMapping(value = Array("/properties"), method = Array(RequestMethod.GET))
  def propertiesController(): String = {
    logger.info(configService.getMessage())
    configService.getMessage()
  }
}
