package org.apache.spark.util

object TempDirUtil {
  /**
    * Allows for creating temporary directory that will be properly deleted using Spark's shutdown hooks
    * @return temp dir path
    */
  def createTempDir(): String = {
    Utils.createTempDir().getAbsolutePath
  }
}
