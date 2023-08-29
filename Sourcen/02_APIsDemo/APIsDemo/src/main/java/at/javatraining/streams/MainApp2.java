package at.javatraining.streams;

import at.javatraining.common.Measurement;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;

import java.util.Map;

public class MainApp2 {
    public static void main(String[] args) {
        Map<String, Object> properties = Map.of(
                StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                StreamsConfig.APPLICATION_ID_CONFIG, "application2",
                StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1000
        );

        StreamsBuilder builder = new StreamsBuilder();
        builder
            .stream( // KStream<String, Measurement>
                "iot.measurements.filtered",
                       Consumed.with(Serdes.String(), new JsonSerde<>(Measurement.class)) // key, value
                )

                // uses sensor type as key and measurement value as value
                .map(
                    (key, value) -> KeyValue.pair( // KStream<String, Double>
                        value.getSensorType().name(), // e.g. "HYGROMETER"
                        value.getValue()
                    )
                )

                .groupByKey( // KGroupedStream<String, Double>
                    Grouped.with(Serdes.String(), Serdes.Double())
                ) // KGroupedStream<String, Double>

                // calculate value as mean of past mean value and new value
                .reduce( //KTable<String, Double>
                    (meanValuePast, valueNew) -> (meanValuePast + valueNew) / 2
                )

                .toStream() // KStream<String, Double>

                .foreach(
                        (key, value) -> System.out.println("KEY: " + key + ", VALUE: " + value)
                );

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
