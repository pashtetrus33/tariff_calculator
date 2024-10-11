package ru.fastdelivery.domain.common.distance;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinatesTests {

    @Test
    void testCoordinatesCreation() {
        Latitude latitude = new Latitude(40.7128);
        Longitude longitude = new Longitude(-74.0060);
        Coordinates coordinates = new Coordinates(latitude, longitude);

        assertEquals(latitude, coordinates.latitude());
        assertEquals(longitude, coordinates.longitude());
    }

    @Test
    void testCoordinatesEquality() {
        Latitude latitude1 = new Latitude(40.7128);
        Longitude longitude1 = new Longitude(-74.0060);
        Coordinates coordinates1 = new Coordinates(latitude1, longitude1);

        Latitude latitude2 = new Latitude(40.7128);
        Longitude longitude2 = new Longitude(-74.0060);
        Coordinates coordinates2 = new Coordinates(latitude2, longitude2);

        assertEquals(coordinates1, coordinates2);
        assertEquals(coordinates1.hashCode(), coordinates2.hashCode());

        Longitude longitude3 = new Longitude(10.0);
        Coordinates coordinates3 = new Coordinates(latitude1, longitude3);
        assertNotEquals(coordinates1, coordinates3);
    }

    @Test
    void testCoordinatesToString() {
        Latitude latitude = new Latitude(40.7128);
        Longitude longitude = new Longitude(-74.0060);
        Coordinates coordinates = new Coordinates(latitude, longitude);

        String expectedString = "Coordinates[latitude=Latitude{value=40,712800}, longitude=Longitude{value=-74,006000}]";
        assertEquals(expectedString, coordinates.toString());
    }
}