package pl.akkomar.streamprocessing.spark

import org.apache.log4j.LogManager
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.{OutputMode, ProcessingTime}
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.util.TempDirUtil
import pl.akkomar.streamprocessing.CommonConfig

object StructuredStreamingDemo {
  def main(args: Array[String]): Unit = {

    val log = LogManager.getRootLogger
    log.info("Starting Structured streaming app...")

    val spark = SparkSession
      .builder
      .appName("Structured Streaming aggregation")
      .master("local[1]")
      .config("spark.sql.streaming.checkpointLocation", {
        TempDirUtil.createTempDir()
      })
      .getOrCreate()

    val rawTweets: DataFrame = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", CommonConfig.KafkaBrokerString)
      .option("subscribe", CommonConfig.TweetsTopic)
      .load()

    // We are setting watermark to 1 minute, window duration to 1 minute with 15 seconds slides and output mode to Append,
    // therefore we'll get first output after 1 minute, followed by each next 15 seconds
    val rawTweetsWatermarked = rawTweets.withWatermark("timestamp", "1 minute")
    val topHashTags = HashTagsCount.aggregate(rawTweetsWatermarked) //.sort($"window", $"count".desc)

    val topHashTagsStreamingQuery = topHashTags.writeStream
      .trigger(ProcessingTime("10 seconds"))
      .format("pl.akkomar.streamprocessing.spark.SortingConsoleSinkProvider")
      .queryName("HashTags aggregation")
      .option("numRows", 10)
      .option("truncate", false)
      .outputMode(OutputMode.Append()).start()

    topHashTagsStreamingQuery.awaitTermination()
  }
}

object HashTagsCount {
  def aggregate(rawTweets: DataFrame): DataFrame = {
    val tweets = rawTweets.select(rawTweets("timestamp"), rawTweets("value").cast(StringType))

    val exploded = tweets.select(tweets("timestamp"), explode(split(tweets("value"), " ")).as("word"))
    val hashTags = exploded.filter(exploded("word").startsWith("#")).withColumnRenamed("word", "hashtag")

    val topHashTags = hashTags
      .groupBy(
        window(timeColumn = hashTags("timestamp"), windowDuration = "1 minute", slideDuration = "15 seconds"),
        hashTags("hashtag"))
      .count()

    topHashTags
  }
}
