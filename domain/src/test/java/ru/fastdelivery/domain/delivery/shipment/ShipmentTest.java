package ru.fastdelivery.domain.delivery.shipment;

import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.distance.Coordinates;
import ru.fastdelivery.domain.common.distance.Latitude;
import ru.fastdelivery.domain.common.distance.Longitude;
import ru.fastdelivery.domain.common.length.Length;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;

class ShipmentTest {

    @Test
    void whenSummarizingWeightOfAllPackages_thenReturnSum() {
        var weight1 = new Weight(BigInteger.TEN);
        var weight2 = new Weight(BigInteger.ONE);

        var dimension = new Length(BigInteger.valueOf(1_000));

        var packages = List.of(new Pack(weight1, dimension, dimension, dimension), new Pack(weight2, dimension, dimension, dimension));

        Coordinates destination = new Coordinates(new Latitude(56.1290), new Longitude(40.4053));
        Coordinates departure = new Coordinates(new Latitude(55.7558), new Longitude(37.6173));

        var shipment = new Shipment(packages, new CurrencyFactory(code -> true).create("RUB"), destination, departure);

        var massOfShipment = shipment.weightAllPackages();

        assertThat(massOfShipment.weightGrams()).isEqualByComparingTo(BigInteger.valueOf(11));
    }

    @Test
    void whenSummarizingVolumeOfAllPackages_thenReturnSum() {
        var dimension1 = new Length(BigInteger.valueOf(1000)); // 100 см
        var dimension2 = new Length(BigInteger.valueOf(500));  // 50 см
        var weight1 = new Weight(BigInteger.TEN);
        var weight2 = new Weight(BigInteger.ONE);

        var packages = List.of(
                new Pack(weight1, dimension1, dimension1, dimension1),
                new Pack(weight2, dimension2, dimension2, dimension2)
        );

        Coordinates destination = new Coordinates(new Latitude(56.1290), new Longitude(40.4053));
        Coordinates departure = new Coordinates(new Latitude(55.7558), new Longitude(37.6173));

        var shipment = new Shipment(packages, new CurrencyFactory(code -> true).create("RUB"), destination, departure);

        var totalVolume = shipment.volumeAllPackages();

        // Ожидаемый объем: (100 см)^3 + (50 см)^3
        BigDecimal expectedVolume = BigDecimal.valueOf(1.0)
                .add(BigDecimal.valueOf(0.125));

        assertThat(totalVolume.cubeM().setScale(4, RoundingMode.HALF_UP)).isEqualTo(expectedVolume.setScale(4, RoundingMode.HALF_UP));
    }

    @Test
    void whenNoPackages_thenReturnZeroWeightAndVolume() {
        List<Pack> packages = List.of(); // Пустой список пакетов

        Coordinates destination = new Coordinates(new Latitude(56.1290), new Longitude(40.4053));
        Coordinates departure = new Coordinates(new Latitude(55.7558), new Longitude(37.6173));

        var shipment = new Shipment(packages, new CurrencyFactory(code -> true).create("RUB"), destination, departure);

        var totalWeight = shipment.weightAllPackages();
        var totalVolume = shipment.volumeAllPackages();

        assertThat(totalWeight.weightGrams()).isEqualTo(BigInteger.ZERO);
        assertThat(totalVolume.cubeM().compareTo(BigDecimal.ZERO)).isEqualTo(0);
    }

    @Test
    void whenCalculatingDistanceBetweenTwoPoints_thenReturnsCorrectDistance() {

        double lat1 = 55.7558; // Москва
        double lon1 = 37.6173;
        double lat2 = 56.1290; // Владимир
        double lon2 = 40.4053;

        var weight1 = new Weight(BigInteger.TEN);
        var weight2 = new Weight(BigInteger.ONE);

        var dimension = new Length(BigInteger.valueOf(1_000));

        var packages = List.of(new Pack(weight1, dimension, dimension, dimension), new Pack(weight2, dimension, dimension, dimension));

        Coordinates destination = new Coordinates(new Latitude(lat2), new Longitude(lon2));
        Coordinates departure = new Coordinates(new Latitude(lat1), new Longitude(lon1));

        var shipment = new Shipment(packages, new CurrencyFactory(code -> true).create("RUB"), destination, departure);

        double expectedDistance = 179.0; // Примерное расстояние в километрах
        double actualDistance = shipment.haversine();

        assertThat(actualDistance).isCloseTo(expectedDistance, within(1.0));
    }

    @Test
    void whenPointsAreIdentical_thenDistanceIsZero() {
        double lat = 55.7558;
        double lon = 37.6173;

        var weight1 = new Weight(BigInteger.TEN);
        var weight2 = new Weight(BigInteger.ONE);

        var dimension = new Length(BigInteger.valueOf(1_000));

        var packages = List.of(new Pack(weight1, dimension, dimension, dimension), new Pack(weight2, dimension, dimension, dimension));

        Coordinates destination = new Coordinates(new Latitude(lat), new Longitude(lon));
        Coordinates departure = new Coordinates(new Latitude(lat), new Longitude(lon));

        var shipment = new Shipment(packages, new CurrencyFactory(code -> true).create("RUB"), destination, departure);

        double actualDistance = shipment.haversine();

        assertThat(actualDistance).isEqualTo(0.0);
    }

    @Test
    void whenCalculatingDistanceWithNegativeCoordinates_thenReturnsCorrectDistance() {
        double lat1 = -34.6037; // Буэнос-Айрес
        double lon1 = -58.3816;
        double lat2 = -22.9068; // Рио-де-Жанейро
        double lon2 = -43.1729;

        var weight1 = new Weight(BigInteger.TEN);
        var weight2 = new Weight(BigInteger.ONE);

        var dimension = new Length(BigInteger.valueOf(1_000));

        var packages = List.of(new Pack(weight1, dimension, dimension, dimension), new Pack(weight2, dimension, dimension, dimension));

        Coordinates destination = new Coordinates(new Latitude(lat2), new Longitude(lon2));
        Coordinates departure = new Coordinates(new Latitude(lat1), new Longitude(lon1));

        var shipment = new Shipment(packages, new CurrencyFactory(code -> true).create("RUB"), destination, departure);

        double expectedDistance = 1968.0; // Примерное расстояние в километрах
        double actualDistance = shipment.haversine();

        assertThat(actualDistance).isCloseTo(expectedDistance, within(1.0));
    }
}