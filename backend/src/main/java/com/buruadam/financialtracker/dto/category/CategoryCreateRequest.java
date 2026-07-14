package com.buruadam.financialtracker.dto.category;

import com.buruadam.financialtracker.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryCreateRequest(
        @NotBlank(message = "Category name is required")
        String name,

        @NotNull(message = "Transaction type is required")
        TransactionType type
) {}
