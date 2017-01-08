import sbt._

object Dependencies {
  // Versions
  lazy val sparkVersion = "2.1.0"

  // Libraries
  val twitter4j = "org.twitter4j" % "twitter4j-stream" % "4.0.6"
  val pureConfig = "com.github.melrief" %% "pureconfig" % "0.4.0"
  val kafkaClients = "org.apache.kafka" % "kafka-clients" % "0.10.1.1"
  val sparkStreaming = "org.apache.spark" %% "spark-streaming" % sparkVersion

  // Projects
  val twitterProducerDeps = Seq(twitter4j, pureConfig, kafkaClients)
  val sparkStreamingDemoDeps = Seq(sparkStreaming)
}