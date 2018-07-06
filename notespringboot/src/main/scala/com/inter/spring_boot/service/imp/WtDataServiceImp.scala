package com.inter.spring_boot.service.imp

import java.io.{ByteArrayOutputStream, OutputStreamWriter}
import java.util
import java.util.ArrayList
import java.util.stream.Collectors

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.stream.JsonWriter
import com.inter.common.LoadJson
import com.inter.common.utils.ResultSetAdapter
import com.inter.common.utils.dbutil.QueryExecutor
import com.inter.dao.Env
import com.inter.spring_boot.service.WtDataService
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, LocatedFileStatus, Path, RemoteIterator}
import org.apache.hadoop.io.IOUtils
import org.springframework.stereotype.Service
import parquet.column.page.PageReadStore
import parquet.example.data.Group
import parquet.example.data.simple.convert.GroupRecordConverter
import parquet.filter2.compat.{FilterCompat, RowGroupFilter}
import parquet.format.converter.ParquetMetadataConverter
import parquet.hadoop.{ParquetFileReader, ParquetInputFormat}
import parquet.hadoop.metadata.BlockMetaData
import parquet.hadoop.metadata.ParquetMetadata
import parquet.io.ColumnIOFactory
import parquet.io.MessageColumnIO
import parquet.io.RecordReader
import parquet.schema.{GroupType, MessageType}

import scala.collection.JavaConversions._

@Service
class WtDataServiceImp extends WtDataService {
  private val maxRow = 10000L

  @throws(classOf[Exception])
  def getPreProperties(dataType: String): LinkedTreeMap[String, String] = {
    val sql = "SELECT hdfs_path, field_prefix FROM config.datatype_config_kudu WHERE datatype_id='" + dataType + "'  LIMIT 1";
    val gson: Gson = new Gson()
    val arrayList: ArrayList[LinkedTreeMap[String, String]] = gson.fromJson(QueryExecutor.exeQuery(sql), classOf[ArrayList[LinkedTreeMap[String, String]]])
    var map: LinkedTreeMap[String, String] = new LinkedTreeMap[String, String]()
    arrayList.map((item: LinkedTreeMap[String, String]) => map.putAll(item))
    map
  }

  @throws(classOf[Exception])
  def parseColumn(colList: String, field_prefix: String): String = {
    ResultSetAdapter.columnReplacer("-", "_",
      ResultSetAdapter.columnReplacer(" ", "_",
        ResultSetAdapter.columnReplacer("]", "",
          ResultSetAdapter.columnReplacer("[", "_",
            ResultSetAdapter.columnPrefixer(field_prefix, colList.split(",").toList)))))
      .stream().collect(Collectors.joining(","))
  }

  @throws(classOf[Exception])
  override def getWtData(token: String,
                         dataType: String,
                         colList: String,
                         wfList: String,
                         wtList: String,
                         startDate: String,
                         endDate: String): String = {

    System.setProperty("HADOOP_USER_NAME", "user27785")
    println(System.getProperty("HADOOP_USER_NAME"))
    val config: Configuration = new Configuration(false)

    val conf: Env = LoadJson.getConfig()

    config.set("fs.defaultFS", "hdfs://" + conf.hdfs_NM2Host)
    println(conf.hdfs_NM2Host)
    config.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem")
    config.set("dfs.nameservices", conf.hdfs_nameservices);
    config.set("dfs.ha.namenodes.nameservice1", conf.hdfs_NM1Host + "," + conf.hdfs_NM2Host)
    config.set("dfs.namenode.rpc-address.nameservice1.namenode1", conf.hdfs_NM1Host + ":" + conf.hdfs_NM1_port)
    config.set("dfs.namenode.rpc-address.nameservice1.namenode2", conf.hdfs_NM2Host + ":" + conf.hdfs_NM2_port)
    config.set("dfs.client.failover.proxy.provider.nameservice1", "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider")

    val map: LinkedTreeMap[String, String] = getPreProperties(dataType)
    val str_path: String = map.get("hdfs_path") + "/" + wfList + "/" + wtList + "/" + startDate.substring(0, 4) + "/"
    val path = new Path(str_path)

    val columns = parseColumn(colList, map.get("field_prefix"))
    val paths: Array[String] = columns.split(",")

    val fileSystem: FileSystem = FileSystem.get(path.toUri, config, "user27785")
    val fileStatusListIterator: RemoteIterator[LocatedFileStatus] = fileSystem.listFiles(path, true)

    var currentRowsNum = 0;
    var baos = new ByteArrayOutputStream()
    val writer = new JsonWriter(new OutputStreamWriter(baos, "UTF-8"))

    writer.beginArray()
    while (fileStatusListIterator.hasNext) {
      val parquetFile = fileStatusListIterator.next().getPath
      if (parquetFile.getName.endsWith(".parquet")) {
        val readFooter: ParquetMetadata = ParquetFileReader.readFooter(config, parquetFile, ParquetMetadataConverter.NO_FILTER)
        val schema: GroupType = readFooter.getFileMetaData.getSchema

        var reqSchema: MessageType = null
        for (p: String <- paths)
          reqSchema =
            if (reqSchema != null) reqSchema.union(new MessageType(p, schema.getType(p)))
            else new MessageType(p, schema.getType(p))
        val containRectime: Boolean = reqSchema.containsPath(Array[String]("f_rectime"))
        if (!containRectime)
          reqSchema = reqSchema.union(new MessageType("f_rectime", schema.getType("f_rectime")))

        val filter: FilterCompat.Filter = ParquetInputFormat.getFilter(config)

        val blocks: util.List[BlockMetaData] = RowGroupFilter.filterRowGroups(filter, readFooter.getBlocks, reqSchema)
        val read = new ParquetFileReader(config, readFooter.getFileMetaData, parquetFile, blocks, reqSchema.getColumns)
        var pages: PageReadStore = read.readNextRowGroup()
        while (pages != null) {
          val rows: Long = pages.getRowCount
          val columnIO: MessageColumnIO = new ColumnIOFactory().getColumnIO(reqSchema)
          val recordReader: RecordReader[Group] = columnIO.getRecordReader(pages, new GroupRecordConverter(reqSchema))
          val leftRows: Long = maxRow - currentRowsNum
          for (index <- 0.until((Math.min(rows, leftRows)).asInstanceOf[Int])) {
            val g: Group = recordReader.read()
            val date: String = g.getString("f_rectime", 0).substring(0, 10)
            if (endDate.compareTo(date) >= 0 && startDate.compareTo(date) <= 0) {
              ResultSetAdapter.resultAdapter(writer, g, colList, map.get("field_prefix"), containRectime)
              currentRowsNum += 1
            }
          }
          pages = read.readNextRowGroup()
        }
      }
    }
    if (writer != null) {
      writer.endArray()
      writer.flush()
      writer.close()
    }
    if (fileSystem != null) IOUtils.closeStream(fileSystem)
    if (baos != null) baos.toString() else ""
  }
}