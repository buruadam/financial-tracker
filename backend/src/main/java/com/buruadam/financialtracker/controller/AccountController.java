package com.buruadam.financialtracker.controller;

import com.buruadam.financialtracker.dto.AccountCreateRequest;
import com.buruadam.financialtracker.dto.AccountResponse;
import com.buruadam.financialtracker.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountCreateRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getMyAccounts() {
        List<AccountResponse> responses = accountService.getMyAccounts();
        return ResponseEntity.ok(responses);
    }
}
