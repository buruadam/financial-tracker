package com.buruadam.financialtracker.controller;

import com.buruadam.financialtracker.dto.transaction.TransactionCreateRequest;
import com.buruadam.financialtracker.dto.transaction.TransactionResponseDto;
import com.buruadam.financialtracker.security.CustomUserDetails;
import com.buruadam.financialtracker.service.TransactionService;
import jakarta.validation.Valid;
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
    public ResponseEntity<TransactionResponseDto> createTransaction(@Valid @RequestBody TransactionCreateRequest request, @AuthenticationPrincipal CustomUserDetails currentUserDetails) {
        TransactionResponseDto response = transactionService.createTransaction(request, currentUserDetails.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getMyTransactions(@AuthenticationPrincipal CustomUserDetails currentUserDetails) {
        List<TransactionResponseDto> responses = transactionService.getMyTransactions(currentUserDetails.getId());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByAccount(@PathVariable UUID accountId, @AuthenticationPrincipal CustomUserDetails currentUserDetails) {

        List<TransactionResponseDto> responses = transactionService.getTransactionsByAccount(accountId, currentUserDetails.getId());
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails currentUserDetails) {

        transactionService.deleteTransaction(id, currentUserDetails.getId());
        return ResponseEntity.noContent().build();
    }
}
