package com.buruadam.financialtracker.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionResponseDto(
        UUID id,
        BigDecimal amount,
        String description,
        LocalDate date,
        UUID accountId,
        UUID categoryId
) {
}
