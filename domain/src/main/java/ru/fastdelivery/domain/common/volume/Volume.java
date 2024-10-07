package ru.fastdelivery.domain.common.volume;


import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Общий класс объема
 *
 * @param volumeCubicMeters объем в кубических метрах
 */
public record Volume(BigDecimal volumeCubicMeters) implements Comparable<Volume> {

    public Volume {
        if (isLessThanZero(volumeCubicMeters)) {
            throw new IllegalArgumentException("Volume cannot be below Zero!");
        }
    }

    public BigDecimal cubeM() {
        return volumeCubicMeters
                .divide(BigDecimal.valueOf(1), 100, RoundingMode.HALF_UP);
    }

    private static boolean isLessThanZero(BigDecimal volume) {
        return BigDecimal.ZERO.compareTo(volume) > 0;
    }

    public static Volume zero() {
        return new Volume(BigDecimal.ZERO);
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Volume volume = (Volume) o;
        return volumeCubicMeters.compareTo(volume.volumeCubicMeters) == 0;
    }

    @Override
    public int compareTo(Volume v) {
        return v.volumeCubicMeters.compareTo(volumeCubicMeters);
    }

    public boolean greaterThan(Volume v) {
        return volumeCubicMeters.compareTo(v.volumeCubicMeters) > 0;
    }

    public Volume add(Volume additionalVolume) {
        return new Volume(this.volumeCubicMeters.add(additionalVolume.volumeCubicMeters));
    }
}