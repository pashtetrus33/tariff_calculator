package ru.fastdelivery.domain.common.length;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LengthTest {

    @Test
    @DisplayName("Попытка создать отрицательную длину -> исключение")
    void whenMillimetersBelowZero_thenException() {
        var lengthMillimeters = new BigInteger("-1");
        assertThatThrownBy(() -> new Length(lengthMillimeters))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void equalsTypeWidth_same() {
        var length = new Length(new BigInteger("1000"));
        var lengthSame = new Length(new BigInteger("1000"));

        assertThat(length)
                .isEqualTo(lengthSame)
                .hasSameHashCodeAs(lengthSame);
    }

    @Test
    void equalsNull_false() {
        var length = new Length(new BigInteger("4"));

        assertThat(length).isNotEqualTo(null);
    }

    @ParameterizedTest
    @CsvSource({"1000, 1, -1",
            "199, 199, 0",
            "50, 999, 1"})
    void compareToTest(BigInteger small, BigInteger big, int expected) {
        var lengthSmall = new Length(small);
        var lengthBig = new Length(big);

        assertThat(lengthSmall.compareTo(lengthBig))
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Добавление положительной длинны -> длинна увеличится")
    void whenAddPositiveLength_thenLengthIsIncreased() {
        var lengthBase = new Length(new BigInteger("1000"));
        var actual = lengthBase.add(new Length(new BigInteger("1000")));

        assertThat(actual)
                .isEqualTo(new Length(new BigInteger("2000")));
    }

    @Test
    @DisplayName("Первая длинна больше второй -> true")
    void whenFirstLengthGreaterThanSecond_thenTrue() {
        var lengthBig = new Length(new BigInteger("1001"));
        var lengthSmall = new Length(new BigInteger("1000"));

        assertThat(lengthBig.greaterThan(lengthSmall)).isTrue();
    }

    @Test
    @DisplayName("Запрос количество  -> получено корректное значение")
    void whenGetMeters_thenReceiveMeters() {
        var length = new Length(new BigInteger("1001"));

        var actual = length.meters();

        assertThat(actual).isEqualByComparingTo(new BigDecimal("1.001"));
    }
}