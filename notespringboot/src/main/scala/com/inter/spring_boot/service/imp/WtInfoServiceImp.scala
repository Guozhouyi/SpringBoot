package com.inter.spring_boot.service.imp

import com.inter.common.utils.dbutil.QueryExecutor
import com.inter.spring_boot.service.WtInfoService
import org.springframework.stereotype.Service

@Service
class WtInfoServiceImp extends WtInfoService {
  override def getWtInfosV1(token: String, deviceType: String): String = {
    val sql: String = "SELECT * from scada_mysql.scada_wtinfo_kudu WHERE f_protocolid in " +
      "(SELECT f_protocolid from scada_mysql.scada_wttypeinfo_kudu WHERE scada_wttypeinfo_kudu.f_devicetype=" + Integer.parseInt(deviceType) + ")"
    val prefix: String = QueryExecutor.getColumnPrefix("wtinfo")
    QueryExecutor.exeQuery(sql, prefix)
  }

  override def getWtInfosV2(token: String, wfId: String): String = {
    val sql: String = "SELECT * from scada_mysql.scada_wtinfo_kudu WHERE f_wfid =\'" + wfId + "\'";
    val prefix: String = QueryExecutor.getColumnPrefix("wtinfo")
    QueryExecutor.exeQuery(sql, prefix)
  }
}
