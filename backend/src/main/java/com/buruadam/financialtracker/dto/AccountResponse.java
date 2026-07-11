package com.buruadam.financialtracker.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        String name,
        BigDecimal balance,
        String currency
) {}
