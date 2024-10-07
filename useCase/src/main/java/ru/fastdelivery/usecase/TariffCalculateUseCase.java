package ru.fastdelivery.usecase;

import lombok.RequiredArgsConstructor;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import javax.inject.Named;

@Named
@RequiredArgsConstructor
public class TariffCalculateUseCase {
    private final WeightPriceProvider weightPriceProvider;
    private final VolumePriceProvider volumePriceProvider;

    public Price calc(Shipment shipment) {

        var weightAllPackagesKg = shipment.weightAllPackages().kilograms();
        var volumeAllPackagesCubeM = shipment.volumeAllPackages().cubeM();

        var minimalPrice = weightPriceProvider.minimalPrice();


        Price wightPrice = weightPriceProvider.costPerKg().multiply(weightAllPackagesKg).max(minimalPrice);
        Price volumePrice = volumePriceProvider.costPerCubeM().multiply(volumeAllPackagesCubeM).max(minimalPrice);


        return wightPrice.max(volumePrice);
    }

    public Price minimalPrice() {
        return weightPriceProvider.minimalPrice();
    }
}