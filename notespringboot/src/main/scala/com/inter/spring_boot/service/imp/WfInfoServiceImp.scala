package com.inter.spring_boot.service.imp

import com.inter.common.utils.dbutil.QueryExecutor
import com.inter.spring_boot.service.WfInfoService
import org.springframework.stereotype.Service

@Service
class WfInfoServiceImp extends WfInfoService {
  override def getAllWfInfos(token: String): String = {
    val sql = "SELECT * from scada_mysql.scada_wfinfo_kudu"
    val prefix = QueryExecutor.getColumnPrefix("wfinfo")
    QueryExecutor.exeQuery(sql, prefix)
  }
}
