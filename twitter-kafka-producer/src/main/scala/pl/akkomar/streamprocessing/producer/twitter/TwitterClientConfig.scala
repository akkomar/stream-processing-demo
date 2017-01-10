package pl.akkomar.streamprocessing.producer.twitter

import java.nio.file.Paths

import com.typesafe.config.ConfigFactory
import pureconfig.loadConfig
import twitter4j.conf.Configuration

case class TwitterClientConfig(oAuthConsumerKey: String,
                               oAuthConsumerSecret: String,
                               oAuthAccessToken: String,
                               oAuthAccessTokenSecret: String)

object TwitterClientConfig {
  def getTwitter4jConfig(): Configuration = {
    ConfigFactory.invalidateCaches()
    val app = ConfigFactory.defaultApplication()
    println(app)
    val config: TwitterClientConfig = loadConfig[TwitterClientConfig](
      Paths.get("/home/akomar/dev/stream-processing/twitter-kafka-producer/src/main/resources/application.conf"),
      "twitterClientConfig").get

    new twitter4j.conf.ConfigurationBuilder()
      .setOAuthConsumerKey(config.oAuthConsumerKey)
      .setOAuthConsumerSecret(config.oAuthConsumerSecret)
      .setOAuthAccessToken(config.oAuthAccessToken)
      .setOAuthAccessTokenSecret(config.oAuthAccessTokenSecret)
      .build
  }
}
