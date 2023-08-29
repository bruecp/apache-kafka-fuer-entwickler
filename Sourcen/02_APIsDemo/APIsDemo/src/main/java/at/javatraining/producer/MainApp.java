package at.javatraining.producer;

import at.javatraining.common.Measurement;
import at.javatraining.common.SensorType;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class MainApp {
    public static void main (String[] args) throws Exception {

        List<Measurement> measurements = List.of(
                new Measurement("hm1", SensorType.HYGROMETHER,10),
                new Measurement("tm1", SensorType.THERMOMETER,0),
                new Measurement("hm2", SensorType.HYGROMETHER,11),
                new Measurement("tm2", SensorType.THERMOMETER,1)
        );

        Map<String, Object> properties = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                ProducerConfig.ACKS_CONFIG, "1",
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );

        try (
                Producer<String, Measurement> producer = new KafkaProducer<>(properties)
        ) {
            while (true) {

                for (Measurement measurement : measurements) {
                    measurement.updateValue();

                    ProducerRecord<String, Measurement> record = new ProducerRecord<>(
                            "iot.measurements.native",
                            measurement.getSensorId(),
                            measurement
                    );

                    // Callback mit Future nicht wirklich üblich. Entweder Callback oder Future.get.
                    Future<RecordMetadata> future = producer.send(
                            record,
                            // Callback mit Lambda-Syntax da nur eine einizige Operation im Interface onCompletion
                            (metadata, exception) -> {
                                if (exception != null) {
                                    exception.printStackTrace();
                                    throw new RuntimeException("Can't send record to Kafka: " + exception.getMessage());
                                }

                                System.out.println (
                                        "record sent to Kafka ..." + System.lineSeparator() +
                                        ">> topic=" + metadata.topic() + ", partition= " + metadata.partition() +
                                                ", offset="+ metadata.offset() + System.lineSeparator() +
                                                ">> key=" + record.key() + ", value=" + record.value());

                            }
                    );

            //        future.get(); // blocks until record sent

                    Thread.sleep(3000);
                }
            }
        }
    }
}
