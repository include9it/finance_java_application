package io.include9it.finance_java_application.service;

import io.include9it.finance_java_application.db.AccountEntity;
import io.include9it.finance_java_application.db.TransactionEntity;
import io.include9it.finance_java_application.db.TransactionRepository;
import io.include9it.finance_java_application.dto.Transaction;
import io.include9it.finance_java_application.dto.TransactionRequest;
import io.include9it.finance_java_application.dto.TransactionStatus;
import io.include9it.finance_java_application.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountService accountService;
    private final ExchangeRateService exchangeRateService;
    private final TransactionRepository repository;
    private final TransactionMapper mapper;

    public List<Transaction> getTransactionHistory(UUID accountId, Pageable pageable) {
        Page<TransactionEntity> transactionPage = repository.findAllByAccountId(accountId, pageable);

        return transactionPage.getContent().stream()
                .map(mapper::toTransaction)
                .collect(Collectors.toList());
    }

    public List<TransactionEntity> getAllPendingTransactions() {
        return repository.findAll().stream()
                .filter(transaction -> transaction.getStatus().equals(TransactionStatus.PENDING))
                .toList();
    }

    public ResponseEntity<Void> processTransaction(TransactionRequest request) {
        AccountEntity accountEntity = accountService.getAccountEntity(request.accountId());
        AccountEntity targetAccountEntity = accountService.getAccountEntity(request.targetAccountId());

        if (!targetAccountEntity.getCurrency().equals(request.currency())) {
            throw new IllegalStateException(
                    "Error: Different currencies. Can't process operation"
            );
        }

        TransactionEntity entity = mapper.createTransactionEntity(request, accountEntity, targetAccountEntity);
        UUID transactionId = repository.save(entity).getId();

        processFunds(request, transactionId);

        return ResponseEntity.ok().build();
    }

    @Transactional
    public void processFunds(TransactionRequest request, UUID transactionId) {
        AccountEntity accountEntity = accountService.getAccountEntity(request.accountId());
        AccountEntity targetAccountEntity = accountService.getAccountEntity(request.targetAccountId());

        BigDecimal requestAmount = request.amount();

        if (!accountEntity.getCurrency().equals(request.currency())) {
            double exchangeRate = exchangeRateService.getLatestRate(targetAccountEntity.getCurrency(), accountEntity.getCurrency());

            requestAmount = request.amount().multiply(BigDecimal.valueOf(exchangeRate));
        }

        if (accountEntity.getBalance().compareTo(requestAmount) < 0) {
            updateTransactionStatus(transactionId, TransactionStatus.REJECTED);

            throw new IllegalStateException(
                    "Error: Insufficient funds. Can't process operation"
            );
        }

        accountService.updateAccountBalance(request.accountId(), accountEntity.getBalance().subtract(requestAmount));
        accountService.updateAccountBalance(request.targetAccountId(), targetAccountEntity.getBalance().add(request.amount()));

        updateTransactionStatus(transactionId, TransactionStatus.COMPLETE);
    }

    private void updateTransactionStatus(UUID transactionId, TransactionStatus status) {
        TransactionEntity entity = repository.findById(transactionId)
                .orElseThrow(() -> new IllegalStateException(
                        "Transaction id: " + transactionId + " doesn't exist!"
                ));
        entity.setStatus(status);
        repository.save(entity);
    }
}
