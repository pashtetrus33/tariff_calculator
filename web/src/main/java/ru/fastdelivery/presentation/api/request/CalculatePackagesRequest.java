package ru.fastdelivery.presentation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ru.fastdelivery.domain.common.distance.Coordinates;

import java.util.List;

@Schema(description = "Данные для расчета стоимости доставки")
public record CalculatePackagesRequest(

        @Schema(description = "Список упаковок отправления",
                example = "[{\"weight\": 4056, \"length\": 300, \"width\": 200, \"height\": 150}]")
        @NotNull(message = "Список упаковок не должен быть пустым")
        @NotEmpty(message = "Список упаковок не должен быть пустым")
        @Valid
        List<CargoPackage> packages,

        @Schema(description = "Трехбуквенный код валюты", example = "RUB")
        @NotNull(message = "Код валюты не должен быть пустым")
        String currencyCode,

        @Schema(description = "Координаты пункта назначения",
                example = "{\"latitude\": 73.398660, \"longitude\": 55.027532}")
        @NotNull(message = "Координаты пункта назначения не должны быть пустыми")
        @Valid
        Coordinates destination,

        @Schema(description = "Координаты пункта отправления",
                example = "{\"latitude\": 55.446008, \"longitude\": 65.339151}")
        @NotNull(message = "Координаты пункта отправления не должны быть пустыми")
        @Valid
        Coordinates departure
) {
}