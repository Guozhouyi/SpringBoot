package com.inter.common.utils.dbutil

import java.io.{ByteArrayOutputStream, OutputStreamWriter}
import java.sql.{Connection, DriverManager, ResultSet, Statement}
import java.util.ArrayList
import scala.collection.JavaConversions._

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.stream.JsonWriter
import com.inter.common.LoadJson
import com.inter.common.utils.ResultSetAdapter
import com.inter.dao.Env

object QueryExecutor {
  @throws(classOf[Exception])
  def exeQuery(queryString: String, columnPrefix: String): String = {

    val config: Env = LoadJson.getConfig()
    val connectionUrl = "jdbc:impala://" +
      config.impala_host +
      ":" +
      config.impala_port +
      ";AuthMech=3;UID=" +
      "etl_user" +
      ";PWD=" +
      "123456" +
      ";UseSasl=0;UseNativeQuery=1";
    val jdbcDriverName: String = "com.cloudera.impala.jdbc41.Driver"

    var con: Connection = null
    var rs: ResultSet = null
    var stmt: Statement = null
    try {
      Class.forName(jdbcDriverName)
      con = DriverManager.getConnection(connectionUrl)
      stmt = con.createStatement()
      //The PARQUET_FALLBACK_SCHEMA_RESOLUTION query option allows Impala to look up columns within Parquet files by column name,
      // rather than column order, when necessary.
      // The allowed values are:POSITION (0) ，NAME (1)
      //允许Impala在必要时按列名查找Parquet文件中的列，而不是列顺序。
      stmt.execute("set PARQUET_FALLBACK_SCHEMA_RESOLUTION=name;")
      rs = stmt.executeQuery(queryString)
      //字节数组输出流在内存中创建一个字节数组缓冲区，
      // 所有发送到输出流的数据保存在该字节数组缓冲区中
      //下面的构造方法创建一个32字节（默认大小）的缓冲区。
      val baos = new ByteArrayOutputStream();
      val writer = new JsonWriter(new OutputStreamWriter(baos, "UTF-8"))
      ResultSetAdapter.resultAdapter(writer, rs, columnPrefix)
      writer.flush()
      writer.close()
      baos.toString("UTF-8")
    } finally {
      if (rs != null) rs.close()
      if (stmt != null) stmt.close()
      if (con != null) con.close()
    }
  }

  def exeQuery(queryString: String): String = {
    exeQuery(queryString, null)
  }

  def errorMessageToJson(ex: Exception): Unit = {
    val gson: Gson = new Gson()
    val elements: Throwable = ex.getCause
    println(gson.toJson(elements))
    gson.toJson(elements).toString
  }

  def getColumnPrefix(dataType: String): String = {
    val query = "select field_prefix from config.datatype_config_kudu " +
      "where datatype_id = \'" + dataType + "\'"
    val gson: Gson = new Gson()
    var list: ArrayList[LinkedTreeMap[String, String]] = new ArrayList[LinkedTreeMap[String, String]]
    list = gson.fromJson(exeQuery(query), list.getClass)
    val map: LinkedTreeMap[String, String] = list(0)
    map.get("field_prefix")
  }
}

object QueryExecutorDriver {
  def main(args: Array[String]): Unit = {
    val gson = new Gson()
    val sql: String = "SELECT impala_table_kudu FROM config.datatype_config_kudu where datatype_id in (\"fieldmappingname\") LIMIT 1"

    val result: String = QueryExecutor.exeQuery(sql)
    println(result.toString)

    var arrayList: ArrayList[LinkedTreeMap[String, String]] = new ArrayList[LinkedTreeMap[String, String]]

    arrayList = gson.fromJson(result.toString, arrayList.getClass)
    println(arrayList.get(0).get("impala_table_kudu"))
  }
}
