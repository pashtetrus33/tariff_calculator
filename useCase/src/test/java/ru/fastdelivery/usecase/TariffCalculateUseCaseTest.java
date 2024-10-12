package ru.fastdelivery.usecase;

import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.distance.Coordinates;
import ru.fastdelivery.domain.common.distance.Latitude;
import ru.fastdelivery.domain.common.distance.Longitude;
import ru.fastdelivery.domain.common.length.Length;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TariffCalculateUseCaseTest {

    final WeightPriceProvider weightPriceProvider = mock(WeightPriceProvider.class);
    final VolumePriceProvider volumePriceProvider = mock(VolumePriceProvider.class);
    final Currency currency = new CurrencyFactory(code -> true).create("RUB");

    final TariffCalculateUseCase tariffCalculateUseCase = new TariffCalculateUseCase(weightPriceProvider,
            volumePriceProvider);



    @Test
    @DisplayName("Расчет стоимости доставки -> успешно")
    void whenCalculatePrice_thenSuccessWithVolumeCalculation() {
        var minimalPrice = new Price(BigDecimal.TEN, currency);
        var pricePerKg = new Price(BigDecimal.valueOf(100), currency);
        var pricePerCubeM = new Price(BigDecimal.valueOf(200), currency);

        double lat1 = 55.7558; // Москва
        double lon1 = 37.6173;
        double lat2 = 56.1290; // Владимир
        double lon2 = 40.4053;


        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(weightPriceProvider.costPerKg()).thenReturn(pricePerKg);
        when(volumePriceProvider.costPerCubeM()).thenReturn(pricePerCubeM);

        var shipment = new Shipment(List.of(new Pack(new Weight(BigInteger.valueOf(1200)),
                new Length(BigInteger.valueOf(1_000)),
                new Length(BigInteger.valueOf(1_000)),
                new Length(BigInteger.valueOf(1_000)))),
                new CurrencyFactory(code -> true).create("RUB"),
                new Coordinates(new Latitude(lat1), new Longitude(lon1)),
                new Coordinates(new Latitude(lat2), new Longitude(lon2)));

        var expectedPrice = new Price(BigDecimal.valueOf(240), currency);

        tariffCalculateUseCase.setBaseDistanceTariff(150);

        var actualPrice = tariffCalculateUseCase.calc(shipment);

        assertThat(actualPrice).usingRecursiveComparison()
                .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("Расчет стоимости доставки -> успешно")
    void whenCalculatePrice_thenSuccessWithWeightCalculation() {
        var minimalPrice = new Price(BigDecimal.TEN, currency);
        var pricePerKg = new Price(BigDecimal.valueOf(100), currency);
        var pricePerCubeM = new Price(BigDecimal.valueOf(200), currency);

        double lat1 = 55.7558; // Москва
        double lon1 = 37.6173;
        double lat2 = 56.1290; // Владимир
        double lon2 = 40.4053;

        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(weightPriceProvider.costPerKg()).thenReturn(pricePerKg);
        when(volumePriceProvider.costPerCubeM()).thenReturn(pricePerCubeM);

        var shipment = new Shipment(List.of(new Pack(new Weight(BigInteger.valueOf(1200)),
                new Length(BigInteger.valueOf(1_000)),
                new Length(BigInteger.valueOf(1_000)),
                new Length(BigInteger.valueOf(1_000)))),
                new CurrencyFactory(code -> true).create("RUB"),
                new Coordinates(new Latitude(lat1), new Longitude(lon1)),
                new Coordinates(new Latitude(lat2), new Longitude(lon2)));

        var expectedPrice = new Price(BigDecimal.valueOf(240), currency);

        tariffCalculateUseCase.setBaseDistanceTariff(150);

        var actualPrice = tariffCalculateUseCase.calc(shipment);

        assertThat(actualPrice).usingRecursiveComparison()
                .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("Получение минимальной стоимости -> успешно")
    void whenMinimalPrice_thenSuccess() {
        BigDecimal minimalValue = BigDecimal.TEN;
        var minimalPrice = new Price(minimalValue, currency);
        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);

        var actual = tariffCalculateUseCase.minimalPrice();

        assertThat(actual).isEqualTo(minimalPrice);
    }
}