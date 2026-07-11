package com.buruadam.financialtracker.dto;

import com.buruadam.financialtracker.enums.TransactionType;

public record CategoryRequest(
        String name,
        TransactionType type
) {}
