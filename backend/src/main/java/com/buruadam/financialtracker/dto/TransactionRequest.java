package com.buruadam.financialtracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionRequest(
        BigDecimal amount,
        String description,
        LocalDate date,
        UUID accountId,
        Long categoryId
) {}
