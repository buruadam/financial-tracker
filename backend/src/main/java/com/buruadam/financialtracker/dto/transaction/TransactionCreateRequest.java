package com.buruadam.financialtracker.dto.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionCreateRequest(
        @NotNull(message = "Amount is required")
        BigDecimal amount,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Date is required")
        @PastOrPresent(message = "Date cannot be in the future")
        LocalDate date,

        @NotNull(message = "Account ID is required")
        UUID accountId,

        @NotNull(message = "Category ID is required")
        UUID categoryId
) {
}
