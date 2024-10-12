package ru.fastdelivery.domain.common.distance;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class LongitudeSerializer extends JsonSerializer<Longitude> {

    @Override
    public void serialize(Longitude longitude, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        // Сериализуем значение долготы как число
        gen.writeNumber(longitude.value());
    }
}