package pl.akkomar.streamprocessing.spark

import org.apache.spark.sql.execution.streaming.Sink
import org.apache.spark.sql.sources.{DataSourceRegister, StreamSinkProvider}
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.{DataFrame, SQLContext}

class SortingConsoleSink(options: Map[String, String]) extends Sink {
  // Track the batch id
  private var lastBatchId = -1L

  override def addBatch(batchId: Long, data: DataFrame): Unit = synchronized {
    val batchIdStr = if (batchId <= lastBatchId) {
      s"Rerun batch: $batchId"
    } else {
      lastBatchId = batchId
      s"Batch: $batchId"
    }

    println("-------------------------------------------")
    println(batchIdStr)
    println("-------------------------------------------")
    val collectedData = data.sparkSession.createDataFrame(
      data.sparkSession.sparkContext.parallelize(data.collect()), data.schema)
    collectedData.sort(collectedData("count").desc)
      .show(10, truncate = false)
  }
}

class SortingConsoleSinkProvider extends StreamSinkProvider with DataSourceRegister {
  def createSink(
                  sqlContext: SQLContext,
                  parameters: Map[String, String],
                  partitionColumns: Seq[String],
                  outputMode: OutputMode): Sink = {
    new SortingConsoleSink(parameters)
  }

  def shortName(): String = "sortingConsole"
}
