package org.jiangyu.triplematrixmultiply

import org.apache.log4j.Logger

/**
  * Created by jiangyu on 2017/1/13.
  */
object TripleMatrixMultiplyExample {

  private val logger = Logger.getLogger(this.getClass)

  def main(args: Array[String]) {

//    val firstMatrixFile = this.getClass.getClassLoader.getResource("firstmatrix").getPath
//    val secondMatrixFile = this.getClass.getClassLoader.getResource("secondmatrix").getPath
//    val resultMatrixFile = "./resultMatrix"
    val firstMatrixFile = args(0)
    val secondMatrixFile = args(1)
    val resultMatrixFile = args(2)

    val startTime = System.currentTimeMillis()
    val firstMatrix = TripleMatrixMultiply.load(firstMatrixFile, " ")
    val secondMatrix = TripleMatrixMultiply.load(secondMatrixFile, " ")

    val result = TripleMatrixMultiply.multiply(firstMatrix, secondMatrix).save(resultMatrixFile, " ")
    logger.info(">>>> took " +  (System.currentTimeMillis() - startTime) + "ms" )
  }

}
