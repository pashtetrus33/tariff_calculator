package ru.fastdelivery.domain.delivery.pack;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.length.Length;
import ru.fastdelivery.domain.common.volume.Volume;
import ru.fastdelivery.domain.common.weight.Weight;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PackTest {

    @Test
    void whenWeightMoreThanMaxWeight_thenThrowException() {
        var weight = new Weight(BigInteger.valueOf(150_001));
        var length = new Length(BigInteger.valueOf(1_000));
        var width = new Length(BigInteger.valueOf(1_000));
        var height = new Length(BigInteger.valueOf(1_000));
        assertThatThrownBy(() -> new Pack(weight, length, width, height))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Package can't be more than " + new Weight(BigInteger.valueOf(150_000)));
    }

    @Test
    void whenLengthMoreThanMaxLength_thenThrowException() {
        var weight = new Weight(BigInteger.valueOf(150));
        var length = new Length(BigInteger.valueOf(1_501));
        var width = new Length(BigInteger.valueOf(1_000));
        var height = new Length(BigInteger.valueOf(1_000));
        assertThatThrownBy(() -> new Pack(weight, length, width, height))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Any package dimensions can't be more than " + new Length(BigInteger.valueOf(1_500)));
    }

    @Test
    void whenWidthMoreThanMaxWidth_thenThrowException() {
        var weight = new Weight(BigInteger.valueOf(150));
        var length = new Length(BigInteger.valueOf(1_500));
        var width = new Length(BigInteger.valueOf(1_501));
        var height = new Length(BigInteger.valueOf(1_000));
        assertThatThrownBy(() -> new Pack(weight, length, width, height))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Any package dimensions can't be more than " + new Length(BigInteger.valueOf(1_500)));
    }

    @Test
    void whenHeightMoreThanMaxHeight_thenThrowException() {
        var weight = new Weight(BigInteger.valueOf(150));
        var length = new Length(BigInteger.valueOf(1_500));
        var width = new Length(BigInteger.valueOf(1_500));
        var height = new Length(BigInteger.valueOf(1_501));
        assertThatThrownBy(() -> new Pack(weight, length, width, height))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Any package dimensions can't be more than " + new Length(BigInteger.valueOf(1_500)));
    }

    @Test
    void whenAllArgsLessThanMaxValues_thenObjectCreated() {
        var actual = new Pack(new Weight(BigInteger.valueOf(1_000)),
                new Length(BigInteger.valueOf(1_000)),
                new Length(BigInteger.valueOf(1_000)),
                new Length(BigInteger.valueOf(1_000)));
        assertThat(actual.weight()).isEqualTo(new Weight(BigInteger.valueOf(1_000)));
    }

    @Test
    void testVolumeCalculation() {
        // Задаем параметры упаковки
        Weight weight = new Weight(BigInteger.valueOf(100_000)); // 100 кг
        Length length = new Length(BigInteger.valueOf(1000)); // 100 см
        Length width = new Length(BigInteger.valueOf(500));  // 50 см
        Length height = new Length(BigInteger.valueOf(200)); // 20 см

        Pack pack = new Pack(weight, length, width, height);

        // Вычисляем объем
        Volume volume = pack.volume();

        // Ожидаемый объем в кубических метрах
        BigDecimal expectedVolume = new BigDecimal("0.1"); // (1000 * 500 * 200) / 1_000_000_000
        assertThat(volume.cubeM().setScale(4, RoundingMode.HALF_UP)).isEqualTo(expectedVolume.setScale(4, RoundingMode.HALF_UP));
    }

    @Test
    void testVolumeWithZeroDimensions() {
        Weight weight = new Weight(BigInteger.valueOf(100_000)); // 100 кг
        Length length = new Length(BigInteger.ZERO); // 0 см
        Length width = new Length(BigInteger.valueOf(500));  // 50 см
        Length height = new Length(BigInteger.valueOf(200)); // 20 см

        Pack pack = new Pack(weight, length, width, height);

        // Вычисляем объем
        Volume volume = pack.volume();

        // Ожидаем 0 кубических метров
        assertThat(volume.cubeM().compareTo(BigDecimal.ZERO)).isEqualTo(0);
    }

    @Test
    void testExceedingMaxWeight() {
        Weight weight = new Weight(BigInteger.valueOf(160_000)); // 160 кг, больше 150 кг
        Length length = new Length(BigInteger.valueOf(1000)); // 100 см
        Length width = new Length(BigInteger.valueOf(500));  // 50 см
        Length height = new Length(BigInteger.valueOf(200)); // 20 см

        ThrowableAssert.ThrowingCallable executable = () -> new Pack(weight, length, width, height);
        assertThatThrownBy(executable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Package can't be more than " + new Weight(BigInteger.valueOf(150_000)));
    }

    @Test
    void testExceedingMaxLength() {
        Weight weight = new Weight(BigInteger.valueOf(100_000)); // 100 кг
        Length length = new Length(BigInteger.valueOf(1600)); // 160 см, больше 150 см
        Length width = new Length(BigInteger.valueOf(500));  // 50 см
        Length height = new Length(BigInteger.valueOf(200)); // 20 см

        ThrowableAssert.ThrowingCallable executable = () -> new Pack(weight, length, width, height);
        assertThatThrownBy(executable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Any package dimensions can't be more than " + new Length(BigInteger.valueOf(1_500)));
    }
}