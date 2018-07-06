package com.inter.spring_boot.service

trait ProtoInfoService {
  def getProtoInfosV1(token: String): String
  def getProtoInfosV2(token:String,wtId:String):String
}
