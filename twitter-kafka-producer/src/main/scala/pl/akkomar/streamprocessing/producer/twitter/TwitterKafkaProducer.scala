package pl.akkomar.streamprocessing.producer.twitter


import org.apache.kafka.clients.producer.ProducerRecord
import twitter4j._


object TwitterKafkaProducer extends App {

  val twitterConfig = TwitterClientConfig.getTwitter4jConfig()

  val kafkaProducer = KafkaUtil.getProducer()

  val twitterStream = new TwitterStreamFactory(twitterConfig).getInstance()
  val statusListener = new StatusListener() {
    override def onStallWarning(warning: StallWarning): Unit = println("Stall warning: " + warning)

    override def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit =
      println("Status deletion notice: " + statusDeletionNotice)

    override def onScrubGeo(userId: Long, upToStatusId: Long): Unit =
      println("ScrubGeo: " + userId + "/" + upToStatusId)

    override def onStatus(status: Status): Unit = {
      //      println("@" + status.getUser.getScreenName + ": " + status.getText)
      kafkaProducer.send(new ProducerRecord[String, String]("tweets", status.getText))
    }

    override def onTrackLimitationNotice(numberOfLimitedStatuses: Int): Unit =
      println("Track limitation notice: " + numberOfLimitedStatuses)

    override def onException(ex: Exception): Unit = println("Exception: " + ex)
  }
  twitterStream.addListener(statusListener)
  twitterStream.sample("en")
}
