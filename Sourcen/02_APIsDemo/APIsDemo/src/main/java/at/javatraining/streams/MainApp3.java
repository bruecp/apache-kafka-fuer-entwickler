package at.javatraining.streams;

import java.net.URI;
import java.util.Map;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.Stores;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import com.sun.net.httpserver.HttpServer;

import at.javatraining.common.Measurement;

public class MainApp3 {
	public static void main(String[] args) {
		Map<String, Object> properties = Map.of(
			StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
			StreamsConfig.APPLICATION_ID_CONFIG, "application3",
			StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1000
		);

		StreamsBuilder builder = new StreamsBuilder();
		
		builder
			.stream(   // KStream<String, Measurement>
				"iot.measurements.filtered",
				Consumed.with(Serdes.String(), new JsonSerde<>(Measurement.class))
			)
			.peek(printKeyValue("stream"))
			
			.toTable(   // KTable<String, Measurement>
				Materialized.<String, Measurement> as(Stores.persistentKeyValueStore("measurement-store"))
					.withKeySerde(Serdes.String())
					.withValueSerde(new JsonSerde<>(Measurement.class))
			);
		
		Topology topology = builder.build();
		
		KafkaStreams streams = new KafkaStreams(topology, new StreamsConfig(properties));
	    
	    streams.setUncaughtExceptionHandler(
	    	exception -> {
	    		exception.printStackTrace();
	    		return StreamThreadExceptionResponse.SHUTDOWN_APPLICATION;
	    	}
	    );
	    
	    streams.start();
	    
	    ResourceConfig config = new ResourceConfig(MeasurementResource.class)
	    	.property(ServerProperties.WADL_FEATURE_DISABLE, true)
	    	.register(new AbstractBinder() {
				@Override
				protected void configure() {
					bind(streams).to(KafkaStreams.class);   // enables injection of object (here: streams) in JAX-RS resource
				}
			});
	    
	    HttpServer httpServer = JdkHttpServerFactory.createHttpServer(
	    	URI.create("http://localhost:8080/"),
	    	config
	    );
	    
	    Runtime.getRuntime().addShutdownHook(
		    new Thread(() -> {
		    	httpServer.stop(0);
		    	streams.close();
		    })
		);
	}
	
	private static <K, V> ForeachAction<K, V> printKeyValue(String info) {
		return (key, value) -> System.out.println("[" + info + "] >> key=" + key + ", value=" + value);
	}
}