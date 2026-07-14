package com.buruadam.financialtracker.controller;

import com.buruadam.financialtracker.dto.TransactionRequest;
import com.buruadam.financialtracker.dto.TransactionResponse;
import com.buruadam.financialtracker.security.CustomUserDetails;
import com.buruadam.financialtracker.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody TransactionRequest request, @AuthenticationPrincipal CustomUserDetails currentUserDetails) {
        TransactionResponse response = transactionService.createTransaction(request, currentUserDetails.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAccount(@PathVariable UUID accountId, @AuthenticationPrincipal CustomUserDetails currentUserDetails) {
        List<TransactionResponse> responses = transactionService.getTransactionsByAccount(accountId, currentUserDetails.getId());
        return ResponseEntity.ok(responses);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(@AuthenticationPrincipal CustomUserDetails currentUserDetails) {
        List<TransactionResponse> responses = transactionService.getAllTransactionsForCurrentUser(currentUserDetails.getId());
        return ResponseEntity.ok(responses);
    }
}
