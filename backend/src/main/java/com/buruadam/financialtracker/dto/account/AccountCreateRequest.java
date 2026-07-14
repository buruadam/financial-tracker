package com.buruadam.financialtracker.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.Currency;

public record AccountCreateRequest(
        @NotBlank(message = "Account name cannot be empty")
        String name,

        @NotNull(message = "Initial balance cannot be null")
        @PositiveOrZero(message = "Initial balance cannot be negative")
        BigDecimal balance,

        @NotNull(message = "Currency cannot be null")
        Currency currency
) {}
