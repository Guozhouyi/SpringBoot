package com.inter.spring_boot.controller

import com.inter.spring_boot.service.WfInfoService
import io.swagger.annotations.{ApiImplicitParam, ApiOperation}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{RequestHeader, RequestMapping, RequestMethod, RestController}

@RestController
class WfInfoController @Autowired()(wfInfoService: WfInfoService) {

  @ApiOperation(value = "获取当前所有风场信息")
  @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String")
  @RequestMapping(value = Array("/allWfInfo"), method = Array(RequestMethod.GET), produces = Array("application/json"))
  @throws(classOf[Exception])
  def getWfInfo(@RequestHeader(value = "token") token: String): String = {
    wfInfoService.getAllWfInfos(token.trim)
  }
}
