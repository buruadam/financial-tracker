package com.buruadam.financialtracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        BigDecimal amount,
        String description,
        LocalDate date,
        UUID accountId,
        String accountName,
        Long categoryId,
        String categoryName,
        String transactionType
) {}
