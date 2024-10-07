package ru.fastdelivery.domain.common.length;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LengthFactoryTest {

    @ParameterizedTest(name = "Миллиметры = {arguments} -> объект создан")
    @ValueSource(longs = { 0, 1, 100, 10_000 })
    void whenLengthGreaterThanZero_thenObjectCreated(long amount) {
        var length = new Length(BigInteger.valueOf(amount));

        assertNotNull(length);
        assertThat(length.lengthMillimeters()).isEqualByComparingTo(BigInteger.valueOf(amount));
    }

    @ParameterizedTest(name = "Стоимость = {arguments} -> исключение")
    @ValueSource(longs = { -1, -100, -10_000 })
    @DisplayName("Значение стоимости ниже 0.00 -> исключение")
    void whenLengthLessThanZero_thenThrowException(long amount) {
        assertThatThrownBy(() -> new Length(BigInteger.valueOf(amount)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}