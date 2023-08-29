package at.javatraining.streams;

import at.javatraining.common.Measurement;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.apache.kafka.streams.kstream.Consumed;

import java.util.Map;
import java.util.UUID;

public class MainApp1 {
    public static void main(String[] args) {
        Map<String, Object> properties = Map.of(
                StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
            //    StreamsConfig.APPLICATION_ID_CONFIG, UUID.randomUUID() + "application1",
                StreamsConfig.APPLICATION_ID_CONFIG, "application1",
                StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1000
        );

        StreamsBuilder builder = new StreamsBuilder();
        builder
            .stream(
                "iot.measurements.native",
                       Consumed.with(Serdes.String(), new JsonSerde<>(Measurement.class)) // key, value
                )
            .peek(
                    (key, value) -> System.out.println("[before filter] KEY: " + key + ", VALUE: " + value)
                )
            .filter(
              //      (key, value) -> true
              //      (key, value) -> key.startsWith("tm") // only let tm through or filter out hm
                    (key, value) -> value.getValue() < 1000
            )
            .peek(
                    (key, value) -> System.out.println("[after filter] KEY: " + key + ", VALUE: " + value)
            )
            .to("iot.measurements.filtered");


        Topology topology = builder.build();

        KafkaStreams streams = new KafkaStreams(topology, new StreamsConfig(properties));

        streams.setUncaughtExceptionHandler(
                exception -> {
                    exception.printStackTrace();
                    return StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.SHUTDOWN_APPLICATION;
            }
        );
        streams.start();

        Runtime.getRuntime().addShutdownHook(
                new Thread(streams::close)
        );

    }
}
