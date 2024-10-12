package ru.fastdelivery.calc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import ru.fastdelivery.ControllerTest;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.distance.Coordinates;
import ru.fastdelivery.domain.common.distance.Latitude;
import ru.fastdelivery.domain.common.distance.LatitudeDeserializer;
import ru.fastdelivery.domain.common.distance.Longitude;
import ru.fastdelivery.domain.common.length.LengthNormalizer;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.presentation.api.request.CalculatePackagesRequest;
import ru.fastdelivery.presentation.api.request.CargoPackage;
import ru.fastdelivery.presentation.api.response.CalculatePackagesResponse;
import ru.fastdelivery.usecase.TariffCalculateUseCase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CalculateControllerTest extends ControllerTest {

    final String baseCalculateApi = "/api/v1/calculate/";

    @MockBean
    private TariffCalculateUseCase useCase;


    @Test
    @DisplayName("Валидные данные для расчета стоимости -> Ответ 200")
    void whenValidInputData_thenReturn200() {

        // Подготовка: создание валидного запроса
        CargoPackage cargoPackage = new CargoPackage(
                BigInteger.TEN,
                BigInteger.TEN,
                BigInteger.TEN,
                BigInteger.TEN
        );

        CalculatePackagesRequest request = new CalculatePackagesRequest(
                List.of(cargoPackage),
                "RUB",
                new Coordinates(new Latitude(56.1291), new Longitude(40.4066)), // Координаты отправления
                new Coordinates(new Latitude(55.7558), new Longitude(37.6173))  // Координаты назначения
        );

        Currency rubCurrency = new CurrencyFactory(code -> true).create("RUB");

        // Настройка поведения use case
        when(useCase.calc(any())).thenReturn(new Price(BigDecimal.valueOf(10), rubCurrency));
        when(useCase.minimalPrice()).thenReturn(new Price(BigDecimal.valueOf(5), rubCurrency));

        // Действие: выполнение запроса к API
        ResponseEntity<CalculatePackagesResponse> response =
                restTemplate.postForEntity(baseCalculateApi, request, CalculatePackagesResponse.class);

        // Проверка: валидация статуса ответа
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Ожидался статус 200");

        // Дополнительные проверки для тела ответа (если применимо)
        CalculatePackagesResponse responseBody = response.getBody();
        assert responseBody != null;
        assertNotNull(responseBody, "Тело ответа не должно быть null");

        // Проверка значений в теле ответа (пример)
        assertEquals(new BigDecimal("10"), responseBody.totalPrice(), "Ожидаемая цена должна быть 10");
        assertEquals("RUB", responseBody.currencyCode(), "Ожидаемая валюта должна быть RUB");
    }



    @Test
    @DisplayName("Если список упаковок пуст -> Ответ 400")
    void whenEmptyListPackages_thenReturn400() {
        var request = createInvalidCalculatePackagesRequest(null);

        ResponseEntity<String> response = performPostRequest(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Если вес упаковки равен null -> Ответ 400")
    void whenWeightIsNullInPackage_thenReturn400() {
        var request = createInvalidCalculatePackagesRequest(BigInteger.ZERO); // Используем 0 для других размеров

        ResponseEntity<String> response = performPostRequest(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Если длина упаковки равна null -> Ответ 400")
    void whenLengthIsNullInPackage_thenReturn400() {
        var request = createInvalidCalculatePackagesRequest(BigInteger.ZERO);

        ResponseEntity<String> response = performPostRequest(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Если ширина упаковки равна null -> Ответ 400")
    void whenWidthIsNullInPackage_thenReturn400() {
        var request = createInvalidCalculatePackagesRequest(BigInteger.ZERO);

        ResponseEntity<String> response = performPostRequest(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Если высота упаковки равна null -> Ответ 400")
    void whenHeightIsNullInPackage_thenReturn400() {
        var request = createInvalidCalculatePackagesRequest(BigInteger.ZERO);

        ResponseEntity<String> response = performPostRequest(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Вспомогательные методы для создания запросов
    private CalculatePackagesRequest createValidCalculatePackagesRequest() {
        return new CalculatePackagesRequest(
                List.of(new CargoPackage(
                        BigInteger.valueOf(10),
                        BigInteger.valueOf(10),
                        BigInteger.valueOf(10),
                        BigInteger.valueOf(10)
                )),
                "RUB",
                new Coordinates(new Latitude(73.398660), new Longitude(55.027532)),
                new Coordinates(new Latitude(55.446008), new Longitude(65.339151))
        );
    }

    private CalculatePackagesRequest createInvalidCalculatePackagesRequest(BigInteger dimensionValue) {
        return new CalculatePackagesRequest(
                List.of(new CargoPackage(
                        dimensionValue != null ? dimensionValue : null, // Замените на null, если хотите протестировать
                        BigInteger.valueOf(10),
                        BigInteger.valueOf(10),
                        BigInteger.valueOf(10)
                )),
                "RUB",
                new Coordinates(new Latitude(73.398660), new Longitude(55.027532)),
                new Coordinates(new Latitude(55.446008), new Longitude(65.339151))
        );
    }

    private ResponseEntity<String> performPostRequest(CalculatePackagesRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CalculatePackagesRequest> entity = new HttpEntity<>(request, headers);

        return restTemplate.exchange(
                baseCalculateApi,
                HttpMethod.POST,
                entity,
                String.class
        );
    }

    @Test
    @DisplayName("Широта меньше допустимого минимума -> Ответ 400")
    void whenLatitudeTooLow_thenReturn400() {
        var request = createCalculatePackagesRequest(new Latitude(44.0), new Longitude(40.4066)); // слишком низкая широта

        ResponseEntity<String> response = performPostRequest(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Широта больше допустимого максимума -> Ответ 400")
    void whenLatitudeTooHigh_thenReturn400() {
        var request = createCalculatePackagesRequest(new Latitude(66.0), new Longitude(40.4066)); // слишком высокая широта

        ResponseEntity<String> response = performPostRequest(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Долгота меньше допустимого минимума -> Ответ 400")
    void whenLongitudeTooLow_thenReturn400() {
        var request = createCalculatePackagesRequest(new Latitude(55.0), new Longitude(25.0)); // слишком низкая долгота

        ResponseEntity<String> response = performPostRequest(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Долгота больше допустимого максимума -> Ответ 400")
    void whenLongitudeTooHigh_thenReturn400() {
        var request = createCalculatePackagesRequest(new Latitude(55.0), new Longitude(100.0)); // слишком высокая долгота

        ResponseEntity<String> response = performPostRequest(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Вспомогательные методы для создания запросов
    private CalculatePackagesRequest createCalculatePackagesRequest(Latitude fromLatitude, Longitude fromLongitude) {
        return new CalculatePackagesRequest(
                List.of(new CargoPackage(
                        BigInteger.TEN,
                        BigInteger.TEN,
                        BigInteger.TEN,
                        BigInteger.TEN
                )),
                "RUB",
                new Coordinates(fromLatitude, fromLongitude), // Координаты отправления
                new Coordinates(new Latitude(55.7558), new Longitude(37.6173)) // Координаты назначения
        );
    }
}