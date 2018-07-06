package com.inter.dao

case class Env(environment: String,
               impala_host: String,
               impala_port: String,
               hdfs_NM1Host: String,
               hdfs_NM1_port: String,
               hdfs_NM2Host: String,
               hdfs_NM2_port: String,
               hdfs_nameservices: String)