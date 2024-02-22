package io.include9it.finance_java_application.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.include9it.finance_java_application.db.TransactionEntity;
import io.include9it.finance_java_application.db.TransactionRepository;
import io.include9it.finance_java_application.dto.Transaction;
import io.include9it.finance_java_application.dto.TransactionStatus;
import io.include9it.finance_java_application.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TransactionServiceTest {
    @Autowired
    private TransactionService transactionService;
    @MockBean
    private TransactionRepository repository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetTransactionHistory() throws IOException, URISyntaxException {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"));
        var responseJson = TestUtil.readResourceFile("response/transaction_history_response.json");
        List<TransactionEntity> entityList = objectMapper.readValue(responseJson, new TypeReference<List<TransactionEntity>>() {
        });
        Page<TransactionEntity> transactionPage = new PageImpl<>(entityList, pageable, entityList.size());

        when(repository.findAllByAccountId(any(), any())).thenReturn(transactionPage);

        List<Transaction> transactions = transactionService.getTransactionHistory(UUID.randomUUID(), pageable);

        assertEquals(entityList.size(), transactions.size());
        verify(repository).findAllByAccountId(any(), any());
    }

    @Test
    public void testGetAllPendingTransactions() throws IOException, URISyntaxException {
        var responseJson = TestUtil.readResourceFile("response/transaction_history_response.json");
        List<TransactionEntity> entityList = objectMapper.readValue(responseJson, new TypeReference<List<TransactionEntity>>() {
        });
        List<TransactionEntity> transactions = entityList.stream()
                .filter(transaction -> transaction.getStatus().equals(TransactionStatus.PENDING))
                .toList();
        when(repository.findAll()).thenReturn(transactions);

        List<TransactionEntity> pendingTransactions = transactionService.getAllPendingTransactions();

        assertEquals(transactions.size(), pendingTransactions.size());
        assertEquals(TransactionStatus.PENDING, pendingTransactions.get(0).getStatus());
        assertEquals(TransactionStatus.PENDING, pendingTransactions.get(1).getStatus());
    }
}

