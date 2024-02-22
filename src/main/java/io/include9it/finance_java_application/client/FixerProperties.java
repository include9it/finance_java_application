package io.include9it.finance_java_application.client;

import feign.Logger;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static feign.Logger.Level.FULL;

@Data
@Component
@ConfigurationProperties("exchange-rate-service.fixer")
public class FixerProperties {
    private String name;
    private String apiKey;
    private String baseUrl;
    private String apiPath;
    private Logger.Level loggerLevel = FULL;
}
