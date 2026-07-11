package com.buruadam.financialtracker.service;

import com.buruadam.financialtracker.dto.AccountCreateRequest;
import com.buruadam.financialtracker.dto.AccountResponse;
import com.buruadam.financialtracker.entity.Account;
import com.buruadam.financialtracker.entity.User;
import com.buruadam.financialtracker.repository.AccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountResponse createAccount(AccountCreateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

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

    public List<AccountResponse> getMyAccounts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        List<Account> accounts = accountRepository.findByUserId(currentUser.getId());

        List<AccountResponse> responseList = new ArrayList<>();
        for (Account account : accounts) {
            responseList.add(new AccountResponse(
                    account.getId(),
                    account.getName(),
                    account.getBalance(),
                    account.getCurrency().getCurrencyCode()
            ));
        }

        return responseList;
    }
}
