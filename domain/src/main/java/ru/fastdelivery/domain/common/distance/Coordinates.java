package ru.fastdelivery.domain.common.distance;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Координаты")
public record Coordinates(Latitude latitude, Longitude longitude) {
}