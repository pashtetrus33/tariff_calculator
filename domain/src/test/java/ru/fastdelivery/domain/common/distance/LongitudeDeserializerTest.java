package ru.fastdelivery.domain.common.distance;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class LongitudeDeserializerTest {

    private LongitudeDeserializer deserializer;
    private double min;
    private double max;

    @BeforeEach
    void setUp() {
        deserializer = new LongitudeDeserializer();
        min = 30.0;
        max = 96.0;

        deserializer.min = min;
        deserializer.max = max;
    }

    @Test
    void testDeserializeValidLongitude() throws Exception {
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getDoubleValue()).thenReturn(45.0);

        Longitude longitude = deserializer.deserialize(parser, Mockito.mock(DeserializationContext.class));
        assertEquals(45.0, longitude.value());
    }

    @Test
    void testDeserializeInvalidLongitudeTooLow() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getDoubleValue()).thenReturn(-200.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            deserializer.deserialize(parser, Mockito.mock(DeserializationContext.class));
        });

        assertEquals("Долгота должна быть в диапазоне от 30.0 до 96.0.", exception.getMessage());
    }

    @Test
    void testDeserializeInvalidLongitudeTooHigh() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getDoubleValue()).thenReturn(200.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            deserializer.deserialize(parser, Mockito.mock(DeserializationContext.class));
        });

        assertEquals("Долгота должна быть в диапазоне от 30.0 до 96.0.", exception.getMessage());
    }
}