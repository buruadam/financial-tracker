package com.buruadam.financialtracker.controller;

import com.buruadam.financialtracker.dto.TransactionRequest;
import com.buruadam.financialtracker.dto.TransactionResponse;
import com.buruadam.financialtracker.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.createTransaction(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAccount(@PathVariable UUID accountId) {
        List<TransactionResponse> responses = transactionService.getTransactionsByAccount(accountId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        List<TransactionResponse> responses = transactionService.getAllTransactionsForCurrentUser();
        return ResponseEntity.ok(responses);
    }
}
