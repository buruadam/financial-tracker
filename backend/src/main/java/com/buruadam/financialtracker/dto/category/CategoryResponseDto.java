package com.buruadam.financialtracker.dto.category;

import com.buruadam.financialtracker.enums.TransactionType;

import java.util.UUID;

public record CategoryResponseDto(
        UUID id,
        String name,
        TransactionType type
) {}
