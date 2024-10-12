package ru.fastdelivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import ru.fastdelivery.config.Config;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.currency.CurrencyPropertiesProvider;
import ru.fastdelivery.domain.common.length.LengthNormalizer;

@SpringBootTest(classes = Config.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ControllerTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @MockBean
    protected CurrencyPropertiesProvider currencyPropertiesProvider;

    @MockBean
    CurrencyFactory currencyFactory;

    @MockBean
    LengthNormalizer lengthNormalizer;
}
