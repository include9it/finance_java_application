package io.include9it.finance_java_application.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
public class FixerApiConfig {
    @Bean
    FixerApi fixerApi(ObjectMapper objectMapper, FixerProperties apiProperties) {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder(objectMapper))
                .decoder(new JacksonDecoder(objectMapper))
                .logLevel(apiProperties.getLoggerLevel())
                .logger(new Slf4jLogger())
                .requestInterceptor(requestTemplate -> {
                    requestTemplate.query("access_key", apiProperties.getApiKey());
                })
                .target(FixerApi.class, apiProperties.getBaseUrl());
    }
}