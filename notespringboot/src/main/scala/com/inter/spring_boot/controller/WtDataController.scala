package com.inter.spring_boot.controller

import com.inter.spring_boot.service.WtDataService
import io.swagger.annotations.{ApiImplicitParam, ApiImplicitParams, ApiOperation}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation._

@RestController
class WtDataController @Autowired()(wtDataService: WtDataService) {

  @ApiOperation(value = "读取类型数据", notes = "最多返回10000条数据")
  @RequestMapping(value = Array("/WtData"), method = Array(RequestMethod.POST), consumes = Array(MediaType.ALL_VALUE), produces = Array("application/json"))
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", dataTypeClass = classOf[String]),
    new ApiImplicitParam(name = "dataType", value = "数据类型", required = true, dataType = "String", dataTypeClass = classOf[String]),
    new ApiImplicitParam(name = "colList", value = "column列表逗号分隔", required = true, dataType = "String", dataTypeClass = classOf[String]),
    new ApiImplicitParam(name = "wfList", value = "风场id", required = true, dataType = "String", dataTypeClass = classOf[String]),
    new ApiImplicitParam(name = "wtList", value = "风机id列表逗号分隔", required = true, dataType = "String", dataTypeClass = classOf[String]),
    new ApiImplicitParam(name = "startDate", value = "开始日期yyy-MM-dd", required = true, dataType = "String", dataTypeClass = classOf[String]),
    new ApiImplicitParam(name = "endDate", value = "结束日期yyy-MM-dd", required = true, dataType = "String", dataTypeClass = classOf[String])))
  @throws(classOf[Exception])
  def getWfData(@RequestHeader(value = "token") token: String,
                @RequestParam(value = "dataType") dataType: String,
                @RequestParam(value = "colList") colList: String,
                @RequestParam(value = "wfList") wfList: String,
                @RequestParam(value = "wtList") wtList: String,
                @RequestParam(value = "startDate") startDate: String,
                @RequestParam(value = "endDate") endDate: String) =
    wtDataService.getWtData(token.trim(),
      dataType.trim(),
      colList.trim(),
      wfList.trim(),
      wtList.trim(),
      startDate.trim(),
      endDate.trim());
}
