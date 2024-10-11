package ru.fastdelivery.domain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "coordinates")
public class CoordinateProperties {
    private double minLatitude;
    private double maxLatitude;
    private double minLongitude;
    private double maxLongitude;
}