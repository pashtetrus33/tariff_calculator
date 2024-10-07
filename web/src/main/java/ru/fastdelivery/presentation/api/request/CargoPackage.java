package ru.fastdelivery.presentation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigInteger;

public record CargoPackage(

        @NotNull(message = "Weight must not be null")
        @Schema(description = "Вес упаковки, граммы", example = "5667")
        BigInteger weight,  // Вес в граммах

        @NotNull(message = "Length must not be null")
        @Schema(description = "Длина упаковки, миллиметры", example = "300")
        BigInteger length,   // Длина в миллиметрах

        @NotNull(message = "Width must not be null")
        @Schema(description = "Ширина упаковки, миллиметры", example = "200")
        BigInteger width,    // Ширина в миллиметрах

        @NotNull(message = "Height must not be null")
        @Schema(description = "Высота упаковки, миллиметры", example = "150")
        BigInteger height    // Высота в миллиметрах
) {
}