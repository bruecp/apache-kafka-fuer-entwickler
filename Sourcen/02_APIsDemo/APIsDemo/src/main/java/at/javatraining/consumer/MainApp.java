package at.javatraining.consumer;

import at.javatraining.producer.JsonSerializer;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class MainApp {
    public static void main(String[] args) {
        Map<String, Object> properties = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                ConsumerConfig.GROUP_ID_CONFIG, "group1",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true,
                ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000
        );

        try (
                Consumer<String, String> consumer = new KafkaConsumer<>(properties);
        ) {
            consumer.subscribe(List.of("iot.measurements.native"));

            // seek to beginning requires heartbeat which is triggered by first poll();
            consumer.poll(0);
            consumer.seekToBeginning(consumer.assignment());

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(3));

                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(
                        "record received from Kafka ..." + System.lineSeparator() +
                        ">> topic=" + record.topic() + ", partition=" + record.partition() + ", offset=" + record.offset() + System.lineSeparator() +
                                ">> key=" + record.key() + ", value=" + record.value()
                    );

                    DocumentContext document = JsonPath.parse(record.value());
                    String sensorId = document.read("$.sensorid");
                    String sensorType = document.read("$.sensortype");
                    double value = document.read("$.value", Double.class);

                    if (value < 1000) {
                        System.out.println(sensorType + ": " + value);
                    }
                }
            }
        }
    }
}
