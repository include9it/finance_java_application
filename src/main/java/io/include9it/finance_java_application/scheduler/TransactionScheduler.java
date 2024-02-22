package io.include9it.finance_java_application.scheduler;

import io.include9it.finance_java_application.mapper.TransactionMapper;
import io.include9it.finance_java_application.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionScheduler {

    private final TransactionService service;
    private final TransactionMapper mapper;

    @Scheduled(cron = "0 0 * * * ?")
    public void updatePendingTransactions() {
        log.info("Updating pending transactions...");

        service.getAllPendingTransactions().stream()
                .peek(transaction -> {
                    service.processFunds(mapper.toRequest(transaction), transaction.getId());
                });
    }
}
