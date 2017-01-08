import Dependencies._

lazy val commonSettings = Seq(
  organization := "pl.akkomar",
  version := "0.1.0",
  scalaVersion := "2.11.8"
)

lazy val `stream-processing-demo` = (project in file(".")).
  aggregate(`twitter-kafka-producer`).
  settings(commonSettings: _*)

lazy val `twitter-kafka-producer` = project.
  settings(commonSettings: _*).
  settings(
    libraryDependencies ++= twitterProducerDeps
  )


lazy val `spark-streaming-demo` = project.
  settings(commonSettings: _*).
  settings(
    libraryDependencies ++= sparkStreamingDemoDeps
  )
