package io.include9it.finance_java_application.api;

import io.include9it.finance_java_application.dto.Account;
import io.include9it.finance_java_application.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/accounts", produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;

    @GetMapping(path = "{clientId}")
    public List<Account> getAccountList(@PathVariable("clientId") UUID clientId) {
        return service.getAccountList(clientId);
    }
}