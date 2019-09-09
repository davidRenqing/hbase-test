package test

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}

import scala.collection.mutable.ListBuffer

object hbaseContectTest {

    def main(args: Array[String]): Unit = {

        batchByFlush()
    }

    /**gcs:
      * 使用普通的 put 的批量插入数据的方法
      * */
    def batchByputList(): Unit ={
        val conf = HBaseConfiguration.create
        conf.set("hbase.zookeeper.quorum","ip-10-10-25-170.ap-southeast-1.compute.internal")
        conf.set("hbase.zookeeper.property.clientPort","2181")
        var conn = HConnectionManager.createConnection(conf)
        val table = conn.getTable(TableName.valueOf("geo-test"))
        val admin = new HBaseAdmin(conf)

        var putList = ListBuffer[org.apache.hadoop.hbase.client.Put]()


        for(i <- 0 until 1000000){
            val put = new Put(Bytes.toBytes(s"${i}"))
            put.addColumn(Bytes.toBytes("column-family"),Bytes.toBytes(s"${i+1}"),Bytes.toBytes(i.toString))

            putList +=put
        }



        import scala.collection.JavaConversions._
        val time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date)

        if(admin.tableExists("geo-test")){

            table.put(putList)

            table.close()
            admin.close()
            conn.close()

            val time2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date)

        }else{

            table.close()
            admin.close()
            conn.close()

        }
    }

    /**gcs:
      * 使用 autoFlush，
      * 设定写数据的 buffer 缓冲区的方法
      * */
    def batchByFlush(): Unit ={
        println(s"-------------------------------------------------0")
        val conf = HBaseConfiguration.create()
        conf.set("hbase.zookeeper.quorum", "ip-10-10-25-170.ap-southeast-1.compute.internal")
        conf.set("hbase.zookeeper.property.clientPort", "2181")
        val connection = ConnectionFactory.createConnection(conf)

        val htable = new HTable(conf, TableName.valueOf("geo-test"))
        htable.setAutoFlush(false, false)
        htable.setWriteBufferSize(3 * 1024 * 1024)

        val time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date)
        println(s"---------------------------${time1}----------------------1")
        for(i <- 0 until 1000000){
            val put = new Put(Bytes.toBytes(s"${i}"))
            put.addColumn(Bytes.toBytes("column-family"),Bytes.toBytes(s"${i+2}"),Bytes.toBytes(i.toString))

            htable.put(put)
        }

        val time2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date)
        println(s"------------------------${time2}-------------------------2")

        htable.flushCommits()
        htable.close()
        connection.close()

    }
}
