package ru.fastdelivery.domain.common.distance;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class LatitudeDeserializerTest {

    private LatitudeDeserializer deserializer;
    private double min;
    private double max;

    @BeforeEach
    void setUp() {
        deserializer = new LatitudeDeserializer();
        min = 45.0;
        max = 65.0;

        deserializer.min = min;
        deserializer.max = max;
    }

    @Test
    void testDeserializeValidLatitude() throws Exception {
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getDoubleValue()).thenReturn(45.0);

        Latitude latitude = deserializer.deserialize(parser, Mockito.mock(DeserializationContext.class));
        assertEquals(45.0, latitude.value());
    }

    @Test
    void testDeserializeInvalidLatitudeTooLow() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getDoubleValue()).thenReturn(-100.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            deserializer.deserialize(parser, Mockito.mock(DeserializationContext.class));
        });

        assertEquals("Широта должна быть в диапазоне от 45.0 до 65.0.", exception.getMessage());
    }

    @Test
    void testDeserializeInvalidLatitudeTooHigh() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getDoubleValue()).thenReturn(100.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            deserializer.deserialize(parser, Mockito.mock(DeserializationContext.class));
        });

        assertEquals("Широта должна быть в диапазоне от 45.0 до 65.0.", exception.getMessage());
    }
}