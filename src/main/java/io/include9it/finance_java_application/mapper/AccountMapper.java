package io.include9it.finance_java_application.mapper;

import io.include9it.finance_java_application.db.AccountEntity;
import io.include9it.finance_java_application.dto.Account;
import org.mapstruct.Mapper;

@Mapper
public interface AccountMapper {

    Account toAccount(AccountEntity accountEntity);
}
