package com.buruadam.financialtracker.service;

import com.buruadam.financialtracker.dto.AccountCreateRequest;
import com.buruadam.financialtracker.dto.AccountResponse;
import com.buruadam.financialtracker.entity.Account;
import com.buruadam.financialtracker.entity.User;
import com.buruadam.financialtracker.exception.ResourceNotFoundException;
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

    public AccountResponse createAccount(AccountCreateRequest request, UUID userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Account account = new Account();
        account.setName(request.name());
        account.setBalance(request.balance());
        account.setCurrency(request.currency());
        account.setUser(currentUser);

        Account savedAccount = accountRepository.save(account);

        return new AccountResponse(
                savedAccount.getId(),
                savedAccount.getName(),
                savedAccount.getBalance(),
                savedAccount.getCurrency().getCurrencyCode()
        );
    }

    public List<AccountResponse> getMyAccounts(UUID userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);

        return accounts.stream()
                .map(account -> new AccountResponse(
                        account.getId(),
                        account.getName(),
                        account.getBalance(),
                        account.getCurrency().getCurrencyCode()
                ))
                .toList();
    }
}
