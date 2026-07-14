package com.buruadam.financialtracker.controller;

import com.buruadam.financialtracker.dto.account.AccountCreateRequest;
import com.buruadam.financialtracker.dto.account.AccountResponseDto;
import com.buruadam.financialtracker.security.CustomUserDetails;
import com.buruadam.financialtracker.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody AccountCreateRequest request, @AuthenticationPrincipal CustomUserDetails currentUserDetails) {
        AccountResponseDto response = accountService.createAccount(request, currentUserDetails.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDto>> getMyAccounts(@AuthenticationPrincipal CustomUserDetails currentUserDetails) {
        List<AccountResponseDto> responses = accountService.getMyAccounts(currentUserDetails.getId());
        return ResponseEntity.ok(responses);
    }
}
