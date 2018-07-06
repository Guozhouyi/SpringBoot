package com.inter.spring_boot.service

trait WtInfoService {
  def getWtInfosV1(token: String, deviceType: String): String

  def getWtInfosV2(token: String, wfId: String): String
}
