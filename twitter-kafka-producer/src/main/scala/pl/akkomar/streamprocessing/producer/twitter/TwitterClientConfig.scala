package pl.akkomar.streamprocessing.producer.twitter

import pureconfig.loadConfig
import twitter4j.conf.Configuration

case class TwitterClientConfig(oAuthConsumerKey: String,
                               oAuthConsumerSecret: String,
                               oAuthAccessToken: String,
                               oAuthAccessTokenSecret: String)

object TwitterClientConfig {
  def getTwitter4jConfig(): Configuration = {
    val config: TwitterClientConfig = loadConfig[TwitterClientConfig]("twitterClientConfig").get

    new twitter4j.conf.ConfigurationBuilder()
      .setOAuthConsumerKey(config.oAuthConsumerKey)
      .setOAuthConsumerSecret(config.oAuthConsumerSecret)
      .setOAuthAccessToken(config.oAuthAccessToken)
      .setOAuthAccessTokenSecret(config.oAuthAccessTokenSecret)
      .build
  }
}
