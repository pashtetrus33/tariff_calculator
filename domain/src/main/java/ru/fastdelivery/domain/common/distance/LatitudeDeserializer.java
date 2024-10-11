package ru.fastdelivery.domain.common.distance;

import com.fasterxml.jackson.databind.JsonDeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LatitudeDeserializer extends JsonDeserializer<Latitude> {

    @Value("${coordinates.latitude.min}")
    double min;

    @Value("${coordinates.latitude.max}")
    double max;

    @Override
    public Latitude deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        double value = p.getDoubleValue();
        validateLatitude(value);
        return new Latitude(value);
    }

    private void validateLatitude(double value) {
        if (value < min || value > max) {
            throw new IllegalArgumentException("Широта должна быть в диапазоне от "
                    + min + " до " + max + ".");
        }
    }
}
