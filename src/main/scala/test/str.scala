package test

import scala.collection.mutable.ArrayBuffer

object str {

    def main(args: Array[String]): Unit = {

        var array1 = new ArrayBuffer[Int]
        array1 +=(1,2,3)

        var array2 = new ArrayBuffer[Int]()
        array2 +=(4,5,6)

        array1 =  array1 ++ array2

        println(array1)

    }
}
