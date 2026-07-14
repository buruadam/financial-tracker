package com.buruadam.financialtracker.dto.account;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponseDto(
        UUID id,
        String name,
        BigDecimal balance,
        String currency
) {
}
