package com.inter.spring_boot.controller

import com.inter.spring_boot.service.WtInfoService
import io.swagger.annotations.{ApiImplicitParam, ApiImplicitParams, ApiOperation}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

@RestController
class WtInfoController @Autowired()(wtInfoService: WtInfoService) {

  @ApiOperation(value = "根据设备类别获取该类所有设备信息")
  @RequestMapping(value = Array("/WtInfo/v1"), method = Array(RequestMethod.GET), produces = Array("application/json"))
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", dataTypeClass = classOf[String]),
    new ApiImplicitParam(name = "deviceType", value = "deviceType", required = true, dataType = "String", dataTypeClass = classOf[String])))
  @throws(classOf[Exception])
  def getWfInfoV1(@RequestHeader(value = "token") token: String,
                  @RequestParam(value = "deviceType") deviceType: String):String =
    wtInfoService.getWtInfosV1(token.trim(), deviceType.trim())

  @ApiOperation(value = "根据风场id获取风场内所有设备信息")
  @RequestMapping(value = Array("/WtInfo/v2"), method = Array(RequestMethod.GET), produces = Array("application/json"))
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", dataTypeClass = classOf[String]),
    new ApiImplicitParam(name = "wfId", value = "wfId", required = true, dataType = "String", dataTypeClass = classOf[String])))
  @throws(classOf[Exception])
  def getWfInfoV2(@RequestHeader(value = "token") token: String,
                  @RequestParam(value = "wfId") wfId: String): String =
    wtInfoService.getWtInfosV2(token.trim(), wfId.trim())
}
