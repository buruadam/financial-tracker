package com.buruadam.financialtracker.dto;

import com.buruadam.financialtracker.enums.TransactionType;

public record CategoryResponse(
        Long id,
        String name,
        TransactionType type
) {}
