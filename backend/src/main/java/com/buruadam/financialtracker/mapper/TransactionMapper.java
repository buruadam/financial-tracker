package com.buruadam.financialtracker.mapper;

import com.buruadam.financialtracker.dto.transaction.TransactionCreateRequest;
import com.buruadam.financialtracker.dto.transaction.TransactionResponseDto;
import com.buruadam.financialtracker.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "category.id", target = "categoryId")
    TransactionResponseDto toResponseDto(Transaction transaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "category", ignore = true)
    Transaction toEntity(TransactionCreateRequest request);
}
