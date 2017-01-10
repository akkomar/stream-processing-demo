package pl.akkomar.streamprocessing.spark

import java.sql.Timestamp

import org.apache.spark.sql.{Encoders, SparkSession}
import org.scalatest._

class HashTagsCountTest extends FlatSpec with BeforeAndAfterEach with GivenWhenThen with Matchers {

  private var spark: SparkSession = _

  override def beforeEach() {
    spark = SparkSession
      .builder
      .appName("Test")
      .master("local[1]")
      .getOrCreate()

  }

  override def afterEach() {
    if (spark != null) {
      spark.stop()
    }
  }

  it should "aggregate raw tweet dataframe by hashtags" in {
    Given("Raw tweets in Kafka connector's format")
    val rawTweets = Seq(
      RawKafkaMessage(null, "some text".getBytes(), "tweets", 0, 0, new Timestamp(2017, 1, 1, 10, 0, 0, 0), 0),
      RawKafkaMessage(null, "some text with #someTag".getBytes(), "tweets", 0, 0, new Timestamp(2017, 1, 1, 10, 0, 1, 0), 0),
      RawKafkaMessage(null, "another #someTag".getBytes(), "tweets", 0, 0, new Timestamp(2017, 1, 1, 10, 0, 16, 0), 0)
    )
    implicit val msgEncoder = Encoders.product[RawKafkaMessage]
    val rawTweetsDataframe = spark.createDataset(rawTweets).toDF()
    println("raw tweets:")
    rawTweetsDataframe.show(false)

    When("We aggregate them by hashtags over 1 minute sliding window that advances every 15 seconds")
    val aggregatedHashTags = HashTagsCount.aggregate(rawTweetsDataframe)

    Then("We have 5 windows in aggregated dataframe")
    //Here we do more thorough test
    aggregatedHashTags.count() shouldBe 5
    println("tweets aggregated by hashtags:")
    aggregatedHashTags.sort("window").show(false)
  }
}

case class RawKafkaMessage(key: Array[Byte], value: Array[Byte], topic: String, partition: Int, offset: Long, timestamp: java.sql.Timestamp, timestampType: Int)
