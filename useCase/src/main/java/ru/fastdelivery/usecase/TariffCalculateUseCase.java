package ru.fastdelivery.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import javax.inject.Named;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Named
@RequiredArgsConstructor
public class TariffCalculateUseCase {

    private final WeightPriceProvider weightPriceProvider;
    private final VolumePriceProvider volumePriceProvider;

    @Value("${app.baseDistanceTariff}")
    private int baseDistanceTariff;

    public Price calc(Shipment shipment) {

        var weightAllPackagesKg = shipment.weightAllPackages().kilograms();
        var volumeAllPackagesCubeM = shipment.volumeAllPackages().cubeM();

        long haversine = shipment.haversine();

        var minimalPrice = weightPriceProvider.minimalPrice();


        Price wightPrice = weightPriceProvider.costPerKg().multiply(weightAllPackagesKg).max(minimalPrice);
        Price volumePrice = volumePriceProvider.costPerCubeM().multiply(volumeAllPackagesCubeM).max(minimalPrice);

        Price baseCost = wightPrice.max(volumePrice);

        if (haversine > baseDistanceTariff) {

            BigDecimal coefficient = BigDecimal.valueOf(((double) haversine / baseDistanceTariff)).setScale(2,RoundingMode.CEILING);

            baseCost = baseCost.multiply(coefficient);
        }


        return baseCost;
    }

    public Price minimalPrice() {
        return weightPriceProvider.minimalPrice();
    }
}