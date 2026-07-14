package com.buruadam.financialtracker.mapper;

import com.buruadam.financialtracker.dto.account.AccountCreateRequest;
import com.buruadam.financialtracker.dto.account.AccountResponseDto;
import com.buruadam.financialtracker.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "currency.currencyCode", target = "currency")
    AccountResponseDto toResponseDto(Account account);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Account toEntity(AccountCreateRequest request);
}
