package ru.fastdelivery.properties.provider;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.usecase.VolumePriceProvider;
import ru.fastdelivery.usecase.WeightPriceProvider;

import java.math.BigDecimal;

/**
 * Настройки базовых цен стоимости перевозки из конфига (по объему)
 */
@ConfigurationProperties("cost.volume.rub")
@Setter
public class PricesRublesVolumeProperties implements VolumePriceProvider {

    private BigDecimal perCubeM;
    private BigDecimal minimal;

    @Autowired
    private CurrencyFactory currencyFactory;

    @Override
    public Price costPerCubeM() {
        return new Price(perCubeM, currencyFactory.create("RUB"));
    }

    @Override
    public Price minimalPrice() {
        return new Price(minimal, currencyFactory.create("RUB"));
    }
}