package io.include9it.finance_java_application.service;

import io.include9it.finance_java_application.client.FixerApi;
import io.include9it.finance_java_application.client.FixerRates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final FixerApi fixerApi;

    public double getLatestRate(String sourceCurrency, String targetCurrency) {
        FixerRates response = fixerApi.getLatestRates(sourceCurrency);

        if (response == null || response.rates() == null) {
            throw new RuntimeException("Unable to fetch exchange rates from Fixer API");
        }

        if (!response.rates().containsKey(targetCurrency)) {
            throw new RuntimeException("Exchange rate not available for target currency: " + targetCurrency);
        }

        return response.rates().get(targetCurrency);
    }
}
