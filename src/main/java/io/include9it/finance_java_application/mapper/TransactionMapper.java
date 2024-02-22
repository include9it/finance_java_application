package io.include9it.finance_java_application.mapper;

import io.include9it.finance_java_application.db.AccountEntity;
import io.include9it.finance_java_application.db.TransactionEntity;
import io.include9it.finance_java_application.dto.Transaction;
import io.include9it.finance_java_application.dto.TransactionRequest;
import io.include9it.finance_java_application.dto.TransactionStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TransactionMapper {
    @Mapping(target = "targetAccountId", source = "targetAccount.id")
    Transaction toTransaction(TransactionEntity transactionEntity);

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "targetAccountId", source = "targetAccount.id")
    TransactionRequest toRequest(TransactionEntity transaction);

    default TransactionEntity createTransactionEntity(TransactionRequest request, AccountEntity accountEntity, AccountEntity targetAccountEntity) {
        var entity = TransactionEntity.builder();
        entity.account(accountEntity);
        entity.targetAccount(targetAccountEntity);
        entity.amount(request.amount());
        entity.currency(request.currency());
        entity.status(TransactionStatus.PENDING);
        return entity.build();
    }
}
