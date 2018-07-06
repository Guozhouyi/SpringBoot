package com.inter.spring_boot.service

trait WtDataService {
  def getWtData(token: String,
                dataType: String,
                colList: String,
                wfList: String,
                wtList: String,
                startDate: String,
                endDate: String): String
}
