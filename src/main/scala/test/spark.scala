//package test
//
//import java.text.SimpleDateFormat
//import java.util.Date
//
//import com.alibaba.fastjson.JSONObject
//import org.apache.commons.lang.StringUtils
//import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
//import org.apache.hadoop.hbase.client.{ConnectionFactory, HTable, Put}
//import org.apache.hadoop.hbase.util.Bytes
//import org.apache.log4j.{Level, Logger}
//import org.apache.spark.{SparkConf, SparkContext}
//import org.apache.spark.rdd.RDD
//import org.apache.spark.sql.SparkSession
//
//import scala.collection.mutable
//import scala.collection.mutable.ArrayBuffer
//
//object spark {
//
//    def main(args: Array[String]): Unit = {
//
//            val conf = new SparkConf().setAppName(s"${this.getClass.getName}:${"1111"}:${"2222"}")
//            val sc = new SparkContext(conf)
//            Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
//            val ss = SparkSession.builder()
//                .appName(this.getClass.getName)
//                .config(conf)
//                .getOrCreate()
//
//            val inputLogDir = ""
//            val inputDf = ss.read.parquet(inputLogDir)
//
//            inputDf.repartition(100).foreachPartition(items =>{
//                println(s"-------------------------------------${items.size}------------0")
//
//                val conf = HBaseConfiguration.create()
//                conf.set("hbase.zookeeper.quorum", "ip-10-10-25-170.ap-southeast-1.compute.internal")
//                conf.set("hbase.zookeeper.property.clientPort", "2181")
//                val connection = ConnectionFactory.createConnection(conf)
//
//                val htable = new HTable(conf, TableName.valueOf("geo-test"))
//                htable.setAutoFlush(false, false)
//                htable.setWriteBufferSize(3 * 1024 * 1024)
//
//                val time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date)
//                println(s"---------------------------${time1}----------------------1")
//
//                try{
//                    for(i <- 0 until 1000){
//                        var str = scala.util.Random.nextInt(10000000).toString
//                        val put = new Put(Bytes.toBytes(s"${i}-${str}"))
//                        put.addColumn(Bytes.toBytes("column-family"),Bytes.toBytes(s"${i+2}"),Bytes.toBytes(i.toString))
//
//                        htable.put(put)
//                    }
//
//                    val time2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date)
//                    println(s"------------------------${time2}-------------------------3")
//
//                    htable.flushCommits()
//                    htable.close()
//                    connection.close()
//                }
//                catch {
//                    case es:Throwable =>
//                        println(s"-------------------------------------------------4")
//                        println(es.toString)
//                }
//
//            })
//
//
//
//            sc.stop()
//            ss.stop()
//
//
//    }
//
//}
