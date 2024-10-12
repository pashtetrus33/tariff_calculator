package ru.fastdelivery.domain.common.distance;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonDeserialize(using = LatitudeDeserializer.class)
@JsonSerialize(using = LatitudeSerializer.class)
public record Latitude(double value) implements Comparable<Latitude>, Serializable {


    @JsonCreator
    public static Latitude fromValues(
            @JsonProperty("value") double value) {
        return new Latitude(value);
    }

    @Override
    public int compareTo(Latitude other) {
        return Double.compare(this.value, other.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Latitude latitude = (Latitude) o;
        return Double.compare(latitude.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public String toString() {
        return String.format("Latitude{value=%.6f}", value);
    }
}