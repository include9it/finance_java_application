package io.include9it.finance_java_application.api;

import io.include9it.finance_java_application.dto.Transaction;
import io.include9it.finance_java_application.dto.TransactionRequest;
import io.include9it.finance_java_application.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @GetMapping(path = "{accountId}")
    public List<Transaction> getTransactionHistory(@PathVariable("accountId") UUID accountId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "createdDate") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        return service.getTransactionHistory(accountId, pageable);
    }

    @PostMapping("/process")
    public ResponseEntity<Void> processTransaction(@Valid @RequestBody TransactionRequest request) {
        return service.processTransaction(request);
    }
}
