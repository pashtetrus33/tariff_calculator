package ru.fastdelivery.domain.common.distance;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = LongitudeDeserializer.class)
public record Longitude(double value) implements Comparable<Longitude> {

    @JsonCreator
    public static Longitude fromValues(
            @JsonProperty("value") double value) {
        return new Longitude(value);
    }

    @Override
    public int compareTo(Longitude other) {
        return Double.compare(this.value, other.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Longitude longitude = (Longitude) o;
        return Double.compare(longitude.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public String toString() {
        return String.format("Longitude{value=%.6f}", value);
    }
}