package com.inter.common.utils

import java.sql.{ResultSet, ResultSetMetaData}

import com.google.gson.{Gson, GsonBuilder}
import com.google.gson.stream.JsonWriter
import parquet.example.data.Group
import parquet.schema.Type

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

object ResultSetAdapter {

  @throws(classOf[Exception])
  def resultAdapter(writer: JsonWriter, rs: ResultSet, columnPrefixed: String): Unit = {
    val gson: Gson = new GsonBuilder().serializeNulls().create()

    val metadata: ResultSetMetaData = rs.getMetaData
    writer.beginArray()
    while (rs.next()) {
      writer.beginObject()
      for (column <- 1.to(metadata.getColumnCount)) {
        val temp: String = if (columnPrefixed == null) metadata.getColumnName(column)
        else metadata.getColumnName(column).substring(columnPrefixed.length)
        writer.name(temp)
        val column_tryp: Class[_] = Class.forName(metadata.getColumnClassName(column))
        gson.toJson(rs.getObject(column), column_tryp, writer)
      }
      writer.endObject()
    }
    writer.endArray()
  }

  @throws(classOf[Exception])
  def resultAdapter(writer: JsonWriter,
                    g: Group,
                    columns: String,
                    columnPrefixed: String,
                    containRectime: Boolean): Unit = {

    var fields: List[String] = columns.split(",").toList
    val gson: Gson = new GsonBuilder().serializeNulls().create()
    writer.beginObject()

    for (index <- 0.until(g.getType.getFieldCount)) {
      val field_type: Type = g.getType.getType(index)
      var fieldName = ""
      fieldName =
        if (columnPrefixed != null) field_type.getName.substring(columnPrefixed.length)
        else field_type.getName
      if (containRectime) {
        fields.remove(fieldName)
        for (i <- 0.until(g.getFieldRepetitionCount(index)))
          if (field_type.isPrimitive)
            writer.name(fieldName).value(g.getValueToString(index, i))
      } else if (!fieldName.equals("rectime")) {
        fields = fields.filter(element => !fieldName.equals(element))
        for (i <- 0.until(g.getFieldRepetitionCount(index)))
          if (field_type.isPrimitive)
            writer.name(fieldName).value(g.getValueToString(index, i))
      }
    }
    for (field <- fields) {
      writer.name(field)
      gson.toJson(null, writer)
    }
    writer.endObject()
  }

  def columnPrefixer(prefix: String, columnList: List[String]): List[String] = {
    var ncl: ListBuffer[String] = ListBuffer()
    columnList.map(col => ncl += (prefix + col))
    ncl.toList
  }

  def columnReplacer(origin: String, target: String, columnList: List[String]): List[String] = {
    var ncl: ListBuffer[String] = ListBuffer()
    columnList.map(col => ncl += (col.replace(origin, target)))
    ncl.toList
  }
}
