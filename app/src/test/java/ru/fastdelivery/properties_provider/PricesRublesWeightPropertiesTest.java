package ru.fastdelivery.properties_provider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.properties.provider.PricesRublesVolumeProperties;
import ru.fastdelivery.properties.provider.PricesRublesWeightProperties;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PricesRublesWeightPropertiesTest {

    public static final BigDecimal PER_KG = BigDecimal.valueOf(50);
    public static final BigDecimal PER_CUBES = BigDecimal.valueOf(150);
    public static final BigDecimal MINIMAL = BigDecimal.valueOf(100);
    public static final String RUB = "RUB";
    final CurrencyFactory currencyFactory = mock(CurrencyFactory.class);
    PricesRublesWeightProperties weightProperties;
    PricesRublesVolumeProperties volumeProperties;

    @BeforeEach
    void init(){
        weightProperties = new PricesRublesWeightProperties();
        volumeProperties = new PricesRublesVolumeProperties();
        weightProperties.setCurrencyFactory(currencyFactory);
        volumeProperties.setCurrencyFactory(currencyFactory);

        volumeProperties.setPerCubeM(PER_CUBES);

        weightProperties.setPerKg(PER_KG);
        weightProperties.setMinimal(MINIMAL);

        var currency = mock(Currency.class);
        when(currency.getCode()).thenReturn(RUB);

        when(currencyFactory.create(RUB)).thenReturn(currency);
    }

    @Test
    void whenCallPricePerKg_thenRequestFromWeightConfig() {
        var actual = weightProperties.costPerKg();

        verify(currencyFactory).create("RUB");
        assertThat(actual.amount()).isEqualByComparingTo(PER_KG);
        assertThat(actual.currency().getCode()).isEqualTo("RUB");
    }

    @Test
    void whenCallPricePerCubes_thenRequestFromVolumeConfig() {
        var actual = volumeProperties.costPerCubeM();

        verify(currencyFactory).create("RUB");
        assertThat(actual.amount()).isEqualByComparingTo(PER_CUBES);
        assertThat(actual.currency().getCode()).isEqualTo("RUB");
    }

    @Test
    void whenCallMinimalPrice_thenRequestFromConfig() {
        var actual = weightProperties.minimalPrice();

        verify(currencyFactory).create("RUB");
        assertThat(actual.amount()).isEqualByComparingTo(MINIMAL);
        assertThat(actual.currency().getCode()).isEqualTo("RUB");
    }
}