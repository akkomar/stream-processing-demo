package pl.akkomar.streamprocessing.producer.twitter

import java.util.Properties

import org.apache.kafka.clients.producer.KafkaProducer
import pl.akkomar.streamprocessing.CommonConfig

object KafkaUtil {
  def getProducer(): KafkaProducer[String, String] = {
    val props = new Properties()
    props.put("bootstrap.servers", CommonConfig.KafkaBrokerString)
    props.put("acks", "all")
    props.put("retries", "0")
    props.put("batch.size", "16384")
    props.put("linger.ms", "1")
    props.put("buffer.memory", "33554432")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    new KafkaProducer[String, String](props)
  }
}
