package pl.akkomar.streamprocessing.spark

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.OutputMode
import pl.akkomar.streamprocessing.CommonConfig

/**
  * Prints raw dataframes fetched from Kafka
  */
object KafkaStreamPreviewApp {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder
      .appName("Kafka stream preview")
      .master("local[1]")
      .getOrCreate()

    val rawTweets = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", CommonConfig.KafkaBrokerString)
      .option("subscribe", CommonConfig.TweetsTopic)
      .load()
    rawTweets.printSchema()
    //    root
    //    |-- key: binary (nullable = true)
    //    |-- value: binary (nullable = true)
    //    |-- topic: string (nullable = true)
    //    |-- partition: integer (nullable = true)
    //    |-- offset: long (nullable = true)
    //    |-- timestamp: timestamp (nullable = true)
    //    |-- timestampType: integer (nullable = true)
    //    val tweets = rawTweets.select($"timestamp", $"value".cast(StringType))//.as[TimestampedTweet]
    //
    //    val hashTags =  tweets.select(explode(split($"value"," "))).filter($"value".startsWith("#")) //tweets.flatMap(tt=>.split(" ")).filter(_.startsWith("#"))

    val rawQuery = rawTweets.writeStream.format("console").outputMode(OutputMode.Append()).start()
    rawQuery.awaitTermination()
  }
}
