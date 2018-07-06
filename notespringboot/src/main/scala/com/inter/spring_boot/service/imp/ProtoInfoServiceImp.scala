package com.inter.spring_boot.service.imp

import java.util.ArrayList

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.inter.common.utils.dbutil.QueryExecutor
import com.inter.spring_boot.service.ProtoInfoService
import org.springframework.stereotype.Service

@Service
class ProtoInfoServiceImp extends ProtoInfoService {
  override def getProtoInfosV1(token: String): String = {

    val sql = "SELECT impala_table_kudu FROM config.datatype_config_kudu where datatype_id in (\"fieldmappingname\") LIMIT 1"
    val gson: Gson = new Gson()
    var arrayList: ArrayList[LinkedTreeMap[String, String]] = new ArrayList[LinkedTreeMap[String, String]]
    val result = QueryExecutor.exeQuery(sql)
    arrayList = gson.fromJson(result, arrayList.getClass)
    val map: LinkedTreeMap[String, String] = arrayList.get(0)
    val querySQL = "SELECT * FROM " + map.get("impala_table_kudu")
    QueryExecutor.exeQuery(querySQL)
  }

  override def getProtoInfosV2(token: String, wtId: String): String = {
    val sql = "SELECT impala_table_kudu FROM config.datatype_config_kudu where datatype_id in (\"fieldmappingname\",\"allprotocolinfo\") order by datatype_id"
    val gson: Gson = new Gson()
    val result = QueryExecutor.exeQuery(sql)
    val map: LinkedTreeMap[String, String] = gson.fromJson(result, classOf[ArrayList[LinkedTreeMap[String, String]]]).get(0)
    val querySQL = "SELECT * FROM " + map.get("impala_table_kudu")
    QueryExecutor.exeQuery(querySQL)
  }
}
