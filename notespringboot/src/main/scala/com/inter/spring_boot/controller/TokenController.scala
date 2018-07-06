package com.inter.spring_boot.controller

import com.inter.spring_boot.service.TokenService
import io.swagger.annotations.{ApiImplicitParam, ApiImplicitParams, ApiOperation}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RequestParam, RestController}

@RestController
class TokenController @Autowired()(tokenService: TokenService) {

  @ApiOperation(value = "生成token")
  @RequestMapping(value = Array("/Token/encrypt"), method = Array(RequestMethod.POST), produces = Array("application/text"))
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "user", value = "user", required = true, dataType = "String", dataTypeClass = classOf[String]),
    new ApiImplicitParam(name = "env", value = "env", required = true, dataType = "String", dataTypeClass = classOf[String])))
  @throws(classOf[Exception])
  def getToken(@RequestParam(value = "user") user: String,
               @RequestParam(value = "env") env: String) = {
    tokenService.genAuthentication(user, env)
  }


    @RequestMapping(value = Array("/Token/decrypt"), method = Array(RequestMethod.POST), produces = Array("application/text"))
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", dataTypeClass = classOf[String])
    @throws(classOf[Exception])
    def decToken(@RequestParam(value = "token") token: String): String = {
      tokenService.decToken(token)
    }
}
