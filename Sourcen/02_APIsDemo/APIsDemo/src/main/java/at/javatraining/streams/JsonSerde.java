package at.javatraining.streams;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;

public class JsonSerde<T> implements Serde<T> {
    private static final ObjectMapper OM = new ObjectMapper();

    // Notwendig um die Klasse <T> im deserializer methode zu binden. Required by Jackson mapper.
    private Class<T> clazz;

    public JsonSerde(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Serializer<T> serializer() {
        return (topic, data) -> {
                try {
                    return OM.writeValueAsBytes(data);
                } catch (
                        JsonProcessingException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Can't serialize data: " + e.getMessage());
                }
        };
    }

    @Override
    public Deserializer<T> deserializer() {
        return (String topic, byte[] data) -> {
            try {
                return OM.readValue(data, clazz);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Can't deserialize data: " + e.getMessage());
            }
        };
    }
}
