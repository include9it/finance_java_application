package io.include9it.finance_java_application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record Transaction(
        UUID targetAccountId,
        BigDecimal amount,
        TransactionStatus status,
        String currency
) {
}
