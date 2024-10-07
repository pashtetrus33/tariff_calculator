package ru.fastdelivery.domain.delivery.shipment;

import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.length.Length;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentTest {

    @Test
    void whenSummarizingWeightOfAllPackages_thenReturnSum() {
        var weight1 = new Weight(BigInteger.TEN);
        var weight2 = new Weight(BigInteger.ONE);

        var dimension = new Length(BigInteger.valueOf(1_000));

        var packages = List.of(new Pack(weight1, dimension, dimension, dimension), new Pack(weight2, dimension, dimension, dimension));
        var shipment = new Shipment(packages, new CurrencyFactory(code -> true).create("RUB"));

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
        var shipment = new Shipment(packages, new CurrencyFactory(code -> true).create("RUB"));

        var totalVolume = shipment.volumeAllPackages();

        // Ожидаемый объем: (100 см)^3 + (50 см)^3
        BigDecimal expectedVolume = BigDecimal.valueOf(1.0)
                .add(BigDecimal.valueOf(0.125));

        assertThat(totalVolume.cubeM().setScale(4, RoundingMode.HALF_UP)).isEqualTo(expectedVolume.setScale(4, RoundingMode.HALF_UP));
    }
}