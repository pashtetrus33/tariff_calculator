package ru.fastdelivery.presentation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Данные для расчета стоимости доставки")
public record CalculatePackagesRequest(
        @Schema(description = "Список упаковок отправления",
                example = "[{\"weight\": 4056, \"length\": 300, \"width\": 200, \"height\": 150}]")
        @NotNull(message = "Packages list must not be null")
        @NotEmpty(message = "Packages list must not be empty")
        @Valid
        List<CargoPackage> packages,

        @Schema(description = "Трехбуквенный код валюты", example = "RUB")
        @NotNull(message = "Currency code must not be null")
        String currencyCode
) {
}