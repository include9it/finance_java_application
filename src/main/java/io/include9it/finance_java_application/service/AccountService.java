package io.include9it.finance_java_application.service;

import io.include9it.finance_java_application.db.AccountEntity;
import io.include9it.finance_java_application.db.AccountRepository;
import io.include9it.finance_java_application.dto.Account;
import io.include9it.finance_java_application.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final AccountMapper mapper;

    public List<Account> getAccountList(UUID clientId) {
        List<AccountEntity> listOfAccounts = repository.findAllByClientId(clientId);

        return listOfAccounts.stream()
                .map(mapper::toAccount)
                .collect(Collectors.toList());
    }

    public AccountEntity getAccountEntity(UUID accountId) {
        return repository.findById(accountId)
                .orElseThrow(() -> new IllegalStateException(
                        "Account id: " + accountId + " doesn't exist!"
                ));
    }

    public void updateAccountBalance(UUID accountId, BigDecimal amount) {
        AccountEntity entity = repository.findById(accountId)
                .orElseThrow(() -> new IllegalStateException(
                        "Account id: " + accountId + " doesn't exist!"
                ));

        entity.setBalance(amount);
    }
}
