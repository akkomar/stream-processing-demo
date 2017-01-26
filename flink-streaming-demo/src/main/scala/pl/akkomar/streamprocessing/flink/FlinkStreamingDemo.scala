package pl.akkomar.streamprocessing.flink

import java.util.Properties

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import pl.akkomar.streamprocessing.CommonConfig

object FlinkStreamingDemo {
  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", CommonConfig.KafkaBrokerString)
    properties.setProperty("group.id", "flink-consumer-group")

    val messageStream: DataStream[String] = env.addSource(new FlinkKafkaConsumer09(
      CommonConfig.TweetsTopic,
      new SimpleStringSchema(),
      properties))

    messageStream.map(_.split(" ")).filter(_.startsWith("#")).timeWindowAll(Time.minutes(1),Time.seconds(15)).

    messageStream.print()

    env.execute()
  }
}
