package pl.akkomar.streamprocessing.producer.twitter


import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import pureconfig._
import twitter4j._

case class TwitterClientConfig(oAuthConsumerKey: String, oAuthConsumerSecret: String, oAuthAccessToken: String, oAuthAccessTokenSecret: String)

object TwitterKafkaProducer extends App {

  val config: TwitterClientConfig = loadConfig[TwitterClientConfig]("twitterClientConfig").get

  val twitterConfig = new twitter4j.conf.ConfigurationBuilder()
    .setOAuthConsumerKey(config.oAuthConsumerKey)
    .setOAuthConsumerSecret(config.oAuthConsumerSecret)
    .setOAuthAccessToken(config.oAuthAccessToken)
    .setOAuthAccessTokenSecret(config.oAuthAccessTokenSecret)
    .build


  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("acks", "all")
  props.put("retries", "0")
  props.put("batch.size", "16384")
  props.put("linger.ms", "1")
  props.put("buffer.memory", "33554432")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)


  val twitterStream = new TwitterStreamFactory(twitterConfig).getInstance()
  val statusListener = new StatusListener() {
    override def onStallWarning(warning: StallWarning): Unit = println("Stall warning: " + warning)

    override def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = println("Status deletion notice: " + statusDeletionNotice)

    override def onScrubGeo(userId: Long, upToStatusId: Long): Unit = println("ScrubGeo: " + userId + "/" + upToStatusId)

    override def onStatus(status: Status): Unit = {
      println("@" + status.getUser.getScreenName + ": " + status.getText)
      producer.send(new ProducerRecord[String, String]("tweets", status.getText))
    }

    override def onTrackLimitationNotice(numberOfLimitedStatuses: Int): Unit = println("Track limitation notice: " + numberOfLimitedStatuses)

    override def onException(ex: Exception): Unit = println("Exception: " + ex)
  }
  twitterStream.addListener(statusListener)
  twitterStream.sample("en")

}
