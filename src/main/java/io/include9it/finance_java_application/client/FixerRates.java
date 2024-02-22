package io.include9it.finance_java_application.client;

import java.time.LocalDate;
import java.util.Map;

public record FixerRates(
        String base,
        LocalDate date,
        Map<String, Double> rates
) {
}
