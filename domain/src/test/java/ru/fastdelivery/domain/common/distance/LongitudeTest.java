package ru.fastdelivery.domain.common.distance;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LongitudeTests {

    @Test
    void testLongitudeCreation() {
        Longitude longitude = new Longitude(30.0);
        assertEquals(30.0, longitude.value());
        assertEquals("Longitude{value=30,000000}", longitude.toString());
    }

    @Test
    void testLongitudeComparison() {
        Longitude lon1 = new Longitude(-10.0);
        Longitude lon2 = new Longitude(20.0);
        Longitude lon3 = new Longitude(-10.0);

        assertTrue(lon1.compareTo(lon2) < 0);
        assertTrue(lon2.compareTo(lon1) > 0);
        assertEquals(0, lon1.compareTo(lon3));
    }

    @Test
    void testLongitudeEqualsAndHashCode() {
        Longitude lon1 = new Longitude(45.0);
        Longitude lon2 = new Longitude(45.0);
        Longitude lon3 = new Longitude(60.0);

        assertEquals(lon1, lon2);
        assertNotEquals(lon1, lon3);
        assertEquals(lon1.hashCode(), lon2.hashCode());
    }
}