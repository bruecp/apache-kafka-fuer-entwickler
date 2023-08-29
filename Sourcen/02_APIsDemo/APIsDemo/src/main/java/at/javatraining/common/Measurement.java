package at.javatraining.common;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCaseStrategy.class)
public class Measurement {
    private String sensorId;
    private SensorType sensorType;
    private double value;

    public void updateValue() {
        // TODO request data from real sensor

        // simulate problem (measurement error) on every 5th request, i.e. 1, 2, 3, 4, 1005, 5, 6, 7, 8, 9, 1010, 10, ...

        if (value < 1000) {
            value = (value + 1) % 100;

            if (value % 5 == 0) {
                value += 1000;
            }
        }
        else {
            value -= 1000;
        }
    }
}

