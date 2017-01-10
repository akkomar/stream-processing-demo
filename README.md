# Comparison of stream processing libraries
Computing aggregations from data streams pulled from Kafka.

## Twitter hashtags
**twitter-kafka-producer** is pushing Twitter's live sample tweet stream into Kafka topic.

We'd like to compute top 10 hashtags during last 1 minute sliding window that advances every 15 seconds.

### Spark - structured streaming
```bash
sbt "spark-streaming-demo/run-main pl.akkomar.streamprocessing.spark.StructuredStreamingDemo"
```
* Structured Streaming is integrated into Dataset and DataFrame APIs (isStreaming property is set to true)
* it adds new operators for windowed aggregation and execution model (e.g. output modes)

## Environment setup
### Start Kafka & Zookeeper
```bash
docker-compose up
```
### Start _twitter-kafka-producer_
```bash
sbt "twitter-kafka-producer/run-main pl.akkomar.streamprocessing.producer.twitter.TwitterKafkaProducer"
```    
### Start console consumer to preview raw data
```bash
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic tweets
```


## TODO
* Flink
* Kafka streams