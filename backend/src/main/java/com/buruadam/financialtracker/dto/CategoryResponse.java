package com.buruadam.financialtracker.dto;

import com.buruadam.financialtracker.enums.TransactionType;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        TransactionType type
) {}
