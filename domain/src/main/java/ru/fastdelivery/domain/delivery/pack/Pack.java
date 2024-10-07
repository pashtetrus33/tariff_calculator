package ru.fastdelivery.domain.delivery.pack;

import ru.fastdelivery.domain.common.length.Length;
import ru.fastdelivery.domain.common.length.LengthNormalizer;
import ru.fastdelivery.domain.common.volume.Volume;
import ru.fastdelivery.domain.common.weight.Weight;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Упаковка груза
 *
 * @param weight вес товаров в упаковке
 * @param length длина товаров в упаковке
 * @param width  ширина товаров в упаковке
 * @param height высота товаров в упаковке
 */
public record Pack(Weight weight, Length length, Length width, Length height) {

    private static final Weight maxWeight = new Weight(BigInteger.valueOf(150_000));
    private static final Length maxLength = new Length(BigInteger.valueOf(1_500));

    public Pack {
        if (weight.greaterThan(maxWeight)) {
            throw new IllegalArgumentException("Package can't be more than " + maxWeight);
        }

        if (length.greaterThan(maxLength) || width.greaterThan(maxLength) || height.greaterThan(maxLength)) {
            throw new IllegalArgumentException("Any package dimensions can't be more than " + maxLength);
        }
    }

    public Volume volume() {
        Length normalizedLength = LengthNormalizer.normalize(length);
        Length normalizedWidth = LengthNormalizer.normalize(width);
        Length normalizedHeight = LengthNormalizer.normalize(height);

        BigInteger volumeInCubicMillimeters = normalizedLength.lengthMillimeters()
                .multiply(normalizedWidth.lengthMillimeters())
                .multiply(normalizedHeight.lengthMillimeters());

        // Проверка на ноль
        if (volumeInCubicMillimeters.equals(BigInteger.ZERO)) {
            return new Volume(BigDecimal.ZERO);
        }

        // Преобразование объема в кубические метры с округлением
        BigDecimal volumeInCubicMeters = new BigDecimal(volumeInCubicMillimeters)
                .divide(BigDecimal.valueOf(1_000_000_000), 10, RoundingMode.HALF_UP);

        // Ограничение до 4 знаков после запятой
        volumeInCubicMeters = volumeInCubicMeters.setScale(4, RoundingMode.HALF_UP);

        return new Volume(volumeInCubicMeters);
    }
}