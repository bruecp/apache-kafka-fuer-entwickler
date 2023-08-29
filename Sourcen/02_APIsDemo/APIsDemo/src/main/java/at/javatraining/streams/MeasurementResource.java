package at.javatraining.streams;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KafkaStreams.State;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

import at.javatraining.common.Measurement;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

// see http://localhost:8080/resources/measurements/<SENSOR-ID>

@Path("/resources/measurements")
@Produces(MediaType.APPLICATION_JSON)
public class MeasurementResource {
	@Inject
	private KafkaStreams streams;
	
	@GET
	@Path("/{sensorid}")
	public Measurement retrieve(@PathParam("sensorid") String sensorId) {
		System.out.println("retrieve() >> sensorId=" + sensorId);
		
		Measurement measurement = null;
		
		if (streams.state() == State.RUNNING) {
			ReadOnlyKeyValueStore<String, Measurement> store = streams.store(
				StoreQueryParameters.fromNameAndType(
					"measurement-store",
					QueryableStoreTypes.keyValueStore()
				)
			);
			
			measurement = store.get(sensorId);
		}
		
		if (measurement == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		
		return measurement;
	}
}