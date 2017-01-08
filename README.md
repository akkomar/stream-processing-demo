# Comparison of stream processing libraries
Computing aggregations from data streams pulled from Kafka.

## Twitter hashtags
**twitter-kafka-producer** is pushing Twitter's live sample tweet stream into Kafka topic.

We'd like to compute top 10 hashtags during last 5 minute moving window, window moves each second.

#### Start Kafka & Zookeeper
    docker-compose up
#### Start console consumer
    bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic tweets
