package org.jiangyu.triplematrixmultiply

import org.apache.log4j.Logger

/**
  * Created by sk on 2017/1/13.
  */
object PerformanceTest {

  private val logger = Logger.getLogger(this.getClass)

  def main(args: Array[String]) {

    if(args.length<5){
      logger.error(">>>> please input 5 parameters: k, nRow1, sparseIndex1, nCol2, sparseIndex2")
      throw new Exception("no enough parameter")
    }
    val firstMatrix = generateRandomMatrix(args(1).toInt, args(0).toInt, args(2).toDouble)
    val secondMatrix = generateRandomMatrix(args(0).toInt, args(3).toInt, args(4).toDouble)

    val startTime = System.currentTimeMillis()
    val result = TripleMatrixMultiply.multiply(firstMatrix, secondMatrix)
    logger.info(">>>> " + args(1) + "x" + args(0) + " matrix(sparseIndex is " + args(2) +") multiply "
    + args(0) + "x" + args(3) + " matrix(sparseIndex is " + args(4) +") took " +  (System.currentTimeMillis() - startTime) + "ms" )

  }

  /**
    * 生成随机矩阵
    * @param nRow
    * @param nCol
    * @param sparseIndex
    * @return
    */
  def generateRandomMatrix(nRow: Int, nCol: Int, sparseIndex: Double): TripleMatrixMultiply ={
    val matrix = new TripleMatrixMultiply()
    for(i<-0 until nRow; j<-0 until nCol){
      val v = scala.math.random
      if(v < sparseIndex){
        matrix.add((i,j), v)
      }
    }
    matrix
  }

}
