import Dependencies._

lazy val commonSettings = Seq(
  organization := "pl.akkomar",
  version := "0.1.0",
  scalaVersion := "2.11.8"
)

lazy val `stream-processing-demo` = (project in file(".")).
  aggregate(`twitter-kafka-producer`, `spark-streaming-demo`, `common-config`).
  settings(commonSettings: _*)

lazy val `twitter-kafka-producer` = project.
  dependsOn(`common-config`).
  settings(commonSettings: _*).
  settings(
    libraryDependencies ++= twitterProducerDeps
  )

lazy val `spark-streaming-demo` = project.
  dependsOn(`common-config`).
  settings(commonSettings: _*).
  settings(
    libraryDependencies ++= sparkStreamingDemoDeps
  )

lazy val `flink-streaming-demo` = project.
  dependsOn(`common-config`).
  settings(commonSettings: _*).
  settings(
    libraryDependencies ++= flinkStreamingDemoDeps
  )




lazy val `common-config` = project.
  settings(commonSettings: _*)
