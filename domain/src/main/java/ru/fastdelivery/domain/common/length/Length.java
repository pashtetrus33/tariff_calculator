package ru.fastdelivery.domain.common.length;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Общий класс длины
 *
 * @param lengthMillimeters длина в миллиметрах
 */
public record Length(BigInteger lengthMillimeters) implements Comparable<Length> {

    public Length {
        if (isLessThanZero(lengthMillimeters)) {
            throw new IllegalArgumentException("Length cannot be below Zero!");
        }
    }

    private static boolean isLessThanZero(BigInteger lengthMillimeters) {
        return BigInteger.ZERO.compareTo(lengthMillimeters) > 0;
    }

    public static Length zero() {
        return new Length(BigInteger.ZERO);
    }

    public BigDecimal meters() {
        return new BigDecimal(lengthMillimeters)
                .divide(BigDecimal.valueOf(1000), 100, RoundingMode.HALF_UP);
    }


    public Length add(Length additionalLength) {
        return new Length(this.lengthMillimeters.add(additionalLength.lengthMillimeters));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Length length = (Length) o;
        return lengthMillimeters.compareTo(length.lengthMillimeters) == 0;
    }

    @Override
    public int hashCode() {
        return lengthMillimeters.hashCode();
    }


    @Override
    public int compareTo(Length w) {
        return w.lengthMillimeters.compareTo(lengthMillimeters);
    }

    public boolean greaterThan(Length w) {
        return lengthMillimeters.compareTo(w.lengthMillimeters) > 0;
    }
}