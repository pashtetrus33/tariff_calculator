package ru.fastdelivery.domain.common.length;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class LengthNormalizer {

    @Value("${app.normalization.value}")
    private static int normalizationValue;

    @Value("${app.normalization.value}")
    public void setNormalizationValue(int normalizationValue) {
        if (normalizationValue <= 0) {
            throw new IllegalArgumentException("Normalization value must be greater than zero!");
        }
        LengthNormalizer.normalizationValue = normalizationValue;
    }

    public static Length normalize(Length length) {
        // Проверка на ноль, чтобы избежать деления на ноль
        if (normalizationValue <= 0) {
            normalizationValue = 50;
            //throw new IllegalArgumentException("Normalization value must be greater than zero!");
        }

        // Получаем длину в миллиметрах из объекта Length
        BigInteger lengthMillimeters = length.lengthMillimeters();

        // Делим lengthMillimeters на normalizationValue и округляем вверх
        BigInteger normalizedLength = lengthMillimeters.add(BigInteger.valueOf(normalizationValue - 1))
                .divide(BigInteger.valueOf(normalizationValue));

        normalizedLength = normalizedLength.multiply(BigInteger.valueOf(normalizationValue)); // Или другое значение, если необходимо

        return new Length(normalizedLength);
    }
}