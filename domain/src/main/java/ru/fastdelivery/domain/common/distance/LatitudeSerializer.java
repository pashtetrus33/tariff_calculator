package ru.fastdelivery.domain.common.distance;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class LatitudeSerializer extends JsonSerializer<Latitude> {

    @Override
    public void serialize(Latitude latitude, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        // Сериализуем значение широты как число
        gen.writeNumber(latitude.value());
    }
}