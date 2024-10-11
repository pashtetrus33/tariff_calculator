package ru.fastdelivery.domain.common.distance;

import com.fasterxml.jackson.databind.JsonDeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LongitudeDeserializer extends JsonDeserializer<Longitude> {

    @Value("${coordinates.longitude.min}")
    double min;

    @Value("${coordinates.longitude.max}")
    double max;

    @Override
    public Longitude deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        double value = p.getDoubleValue();
        validateLongitude(value);
        return new Longitude(value);
    }

    private void validateLongitude(double value) {
        if (value < min || value > max) {
            throw new IllegalArgumentException("Долгота должна быть в диапазоне от "
                    + min + " до " + max + ".");
        }
    }
}
