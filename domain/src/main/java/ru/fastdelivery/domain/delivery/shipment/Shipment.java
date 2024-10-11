package ru.fastdelivery.domain.delivery.shipment;

import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.distance.Coordinates;
import ru.fastdelivery.domain.common.volume.Volume;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;

import java.util.List;

/**
 * @param packages упаковки в грузе
 * @param currency валюта объявленная для груза
 */
public record Shipment(
        List<Pack> packages,
        Currency currency,
        Coordinates destination,
        Coordinates departure
) {

    private void checkCoordinate(double value, double min, double max, String name) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(name + " должна быть в диапазоне от " + min + " до " + max + ".");
        }
    }

    public Weight weightAllPackages() {
        return packages.stream()
                .map(Pack::weight)
                .reduce(Weight.zero(), Weight::add);
    }

    public Volume volumeAllPackages() {
        return packages.stream()
                .map(Pack::volume)
                .reduce(Volume.zero(), Volume::add);
    }

    public long haversine() {
        final double R = 6372.795; // Радиус Земли в километрах
        double lat1 = Math.toRadians(departure.latitude().value());
        double lon1 = Math.toRadians(departure.longitude().value());
        double lat2 = Math.toRadians(destination.latitude().value());
        double lon2 = Math.toRadians(destination.longitude().value());

        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        double a = Math.sin(dlat / 2) * Math.sin(dlat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dlon / 2) * Math.sin(dlon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return Math.round(R * c); // Возвращаем расстояние в км
    }
}