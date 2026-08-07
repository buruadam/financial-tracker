package com.buruadam.financialtracker.dto.transaction;

import com.buruadam.financialtracker.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionResponseDto(
        UUID id,
        BigDecimal amount,
        String description,
        LocalDate date,
        UUID accountId,
        String accountName,
        UUID categoryId,
        String categoryName,
        TransactionType transactionType
) {
}
