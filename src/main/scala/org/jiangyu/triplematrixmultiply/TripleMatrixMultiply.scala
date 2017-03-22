package org.jiangyu.triplematrixmultiply

import java.io._

import scala.collection.mutable.Map
import scala.io.Source

/**
  * Created by jiangyu on 2017/1/13.
  */
class TripleMatrixMultiply(val values: Map[(Int, Int), Double]){

  /**
    * 构造函数
    */
  def this() = {
    this(values = Map[(Int, Int), Double]())
  }

  /**
    * 构造函数
    *
    * @param valueIter 以iterator形式传入三元组
    */
  def this(valueIter: Iterator[((Int, Int), Double)]) = {
    this()
    if(!valueIter.isEmpty){
      valueIter.foreach(v => this.add(v._1, v._2))
    }
  }

  /**
    * 获取特定行的数值
    *
    * @param n 特定行
    * @return
    */
  def getSpecialRowValues(n: Int): Map[(Int, Int), Double] = {
    values.filter(_._1._1==n)
  }

  /**
    * 获取特定列的数值
    *
    * @param n 特定列
    * @return
    */
  def getSpecialColValues(n: Int): Map[(Int, Int), Double] = {
    values.filter(_._1._2==n)
  }

  /**
    * 获取含非零元素的行集合
    *
    * @return
    */
  def getRowSet = values.map(_._1._1).toSet

  /**
    * 获取含非零元素的列集合
    *
    * @return
    */
  def getColSet = values.map(_._1._2).toSet

  /**
    * 矩阵中某个元素加
    *
    * @param loc
    * @param value
    */
  def add(loc: (Int, Int), value: Double): Unit = {
    values(loc) = values.getOrElse(loc, 0.0) + value
  }

  /**
    * 和另一个矩阵加
    *
    * @param anotherMatrix
    * @return
    */
  def add(anotherMatrix: TripleMatrixMultiply): TripleMatrixMultiply = {
    if(!anotherMatrix.values.isEmpty){
      anotherMatrix.values.foreach(kv =>
        add(kv._1, kv._2)
      )
    }
    this
  }

  /**
    * 将矩阵以三元组形式保存
    *
    * @param path 保存路径
    * @param splitStr 分隔符
    */
  def save(path: String, splitStr: String): Unit ={
    val writer = new PrintWriter(new File(path),"UTF-8")
    values.foreach{v =>
      writer.write(v._1._1 + splitStr + v._1._2 + splitStr + v._2 +"\n")
    }
    writer.close
  }

}

object TripleMatrixMultiply{

  /**
    * 从文件导入三元组生成矩阵
    *
    * @param path 文件路径
    * @param splitStr 分隔符
    * @return
    */
  def load(path: String, splitStr: String): TripleMatrixMultiply = {

    val values = Map[(Int, Int), Double]()
    Source.fromFile(path)
      .getLines.map(l=>{
      val strs = l.trim.split(splitStr)
      ((strs(0).toInt, strs(1).toInt), strs(2).toDouble)
    }).foreach(r => values(r._1) = r._2)

    new TripleMatrixMultiply(values)
  }

  /**
    * 多线程实现矩阵乘法
    *
    * @param firstMatrix 第一个矩阵
    * @param secondMatrix 第二个矩阵
    * @return
    */
  def multiply(firstMatrix: TripleMatrixMultiply, secondMatrix: TripleMatrixMultiply): TripleMatrixMultiply = {
    val firstColSet = firstMatrix.getColSet //获取第一个矩阵的含非零元素的列
    val secondRowSet = secondMatrix.getRowSet //获取第二个矩阵的含非零元素的行
    val commonIndexSet = firstColSet.&(secondRowSet) //第一个矩阵的非零列和第二个矩阵的非零行的交集才会生成非零项

    commonIndexSet.toList.par.map(indexe=>{ //利用.par对每一个在commonIndexSet中的index多线程并行处理
    val firstPart = firstMatrix.getSpecialColValues(indexe)
      val secondPart = secondMatrix.getSpecialRowValues(indexe)
      val valueIter = firstPart.flatMap(f =>
        secondPart.map(s=>
          ((f._1._1, s._1._2), f._2*s._2)
        ).toIterator
      )
      new TripleMatrixMultiply(valueIter)
    }).reduce((a,b) => a.add(b))
  }

}
