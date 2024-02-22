package io.include9it.finance_java_application.config;

import io.include9it.finance_java_application.client.FixerApi;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class FixerApiTestConfig {
    @Bean
    public FixerApi fixerApi() {
        return Mockito.mock(FixerApi.class);
    }
}
