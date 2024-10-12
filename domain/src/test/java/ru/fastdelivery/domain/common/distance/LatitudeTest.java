package ru.fastdelivery.domain.common.distance;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LatitudeTest {

    @Test
    void testLatitudeCreation() {
        Latitude latitude = new Latitude(45.0);
        assertEquals(45.0, latitude.value());
        assertEquals("Latitude{value=45,000000}", latitude.toString());
    }

    @Test
    void testLatitudeComparison() {
        Latitude lat1 = new Latitude(30.0);
        Latitude lat2 = new Latitude(45.0);
        Latitude lat3 = new Latitude(30.0);

        assertTrue(lat1.compareTo(lat2) < 0);
        assertTrue(lat2.compareTo(lat1) > 0);
        assertEquals(0, lat1.compareTo(lat3));
    }

    @Test
    void testLatitudeEqualsAndHashCode() {
        Latitude lat1 = new Latitude(30.0);
        Latitude lat2 = new Latitude(30.0);
        Latitude lat3 = new Latitude(45.0);

        assertEquals(lat1, lat2);
        assertNotEquals(lat1, lat3);
        assertEquals(lat1.hashCode(), lat2.hashCode());
    }
}