package io.include9it.finance_java_application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record Account(
        UUID id,
        String currency,
        BigDecimal balance
) {
}
