package com.inter.spring_boot.service

import com.google.gson.{Gson, JsonObject}
import com.inter.spring_boot.dao.Token
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TokenService @Autowired()(token: Token) {

  def genAuthentication(user: String, env: String): String = {
    token.genAuthentication(user, "123456", env)
  }

  @throws(classOf[Exception])
  def decToken(token_pass: String): String = {
    val gson:Gson = new Gson()
    val jsonObject: JsonObject = gson.toJsonTree(token.decodeToken(token_pass)).getAsJsonObject
    jsonObject.toString
  }
}
