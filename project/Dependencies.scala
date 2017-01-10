import sbt._

object Dependencies {
  // Versions
  lazy val sparkVersion = "2.1.0"

  // Libraries
  val twitter4j = "org.twitter4j" % "twitter4j-stream" % "4.0.6"
  val pureConfig = "com.github.melrief" %% "pureconfig" % "0.4.0"
  val kafkaClients = "org.apache.kafka" % "kafka-clients" % "0.10.1.1"
  val sparkCore = "org.apache.spark" %% "spark-core" % sparkVersion
  val sparkSql = "org.apache.spark" %% "spark-sql" % sparkVersion
  val sparkStreaming = "org.apache.spark" %% "spark-streaming" % sparkVersion
  val sparkSqlKafka = "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion

  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test

  // Projects
  val twitterProducerDeps = Seq(twitter4j, pureConfig, kafkaClients)
  val sparkStreamingDemoDeps = Seq(sparkCore, sparkSql, sparkStreaming, sparkSqlKafka, scalaTest)
}