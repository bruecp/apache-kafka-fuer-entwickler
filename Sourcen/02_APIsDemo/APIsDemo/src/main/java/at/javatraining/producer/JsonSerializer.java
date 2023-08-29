package at.javatraining.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.lang.runtime.ObjectMethods;

public class JsonSerializer<T> implements Serializer<T> {

    private static final ObjectMapper OM = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, T data) {
        // transform object to JSON string
        // transform JSON to byte[]
        try {
            return OM.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't serialize data: " + e.getMessage());
        }
    }
}
