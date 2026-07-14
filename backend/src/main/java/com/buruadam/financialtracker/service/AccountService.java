package com.buruadam.financialtracker.service;

import com.buruadam.financialtracker.dto.account.AccountCreateRequest;
import com.buruadam.financialtracker.dto.account.AccountResponseDto;
import com.buruadam.financialtracker.entity.Account;
import com.buruadam.financialtracker.entity.User;
import com.buruadam.financialtracker.exception.ResourceNotFoundException;
import com.buruadam.financialtracker.mapper.AccountMapper;
import com.buruadam.financialtracker.repository.AccountRepository;
import com.buruadam.financialtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    public AccountResponseDto createAccount(AccountCreateRequest request, UUID userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Account account = accountMapper.toEntity(request);
        account.setUser(currentUser);

        Account savedAccount = accountRepository.save(account);

        return accountMapper.toResponseDto(savedAccount);
    }

    public List<AccountResponseDto> getMyAccounts(UUID userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);

        return accounts.stream()
                .map(accountMapper::toResponseDto)
                .toList();
    }
}
