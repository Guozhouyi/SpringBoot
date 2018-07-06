package com.inter.spring_boot.controller

import com.inter.spring_boot.service.ProtoInfoService
import io.swagger.annotations.{ApiImplicitParam, ApiImplicitParams, ApiOperation}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

@RestController
class ProtoInfoController @Autowired()(protoInfoService: ProtoInfoService) {

  @ApiOperation(value = "获取所有信息模型")
  @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String")
  @RequestMapping(value = Array("/PInfo/v1"), method = Array(RequestMethod.GET), produces = Array("application/json"))
  @throws(classOf[Exception])
  def getWfInfoV1(@RequestHeader(value = "token") token: String): String = protoInfoService.getProtoInfosV1(token.trim)


  @ApiOperation(value = "根据设备id获取该设备信息模型")
  @RequestMapping(value = Array("/PInfo/v2"), method = Array(RequestMethod.GET), produces = Array("application/json"))
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", dataTypeClass = classOf[String]),
    new ApiImplicitParam(name = "wtId", value = "wtId", required = true, dataType = "String", dataTypeClass = classOf[String])))
  @throws(classOf[Exception])
  def getWfInfoV2(@RequestHeader(value = "token") token: String,
                  @RequestParam(value = "wtId") wtId: String) = protoInfoService.getProtoInfosV2(token.trim, wtId)
}
