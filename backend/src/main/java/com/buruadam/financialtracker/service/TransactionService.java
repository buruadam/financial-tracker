package com.buruadam.financialtracker.service;

import com.buruadam.financialtracker.dto.TransactionRequest;
import com.buruadam.financialtracker.dto.TransactionResponse;
import com.buruadam.financialtracker.entity.Account;
import com.buruadam.financialtracker.entity.Transaction;
import com.buruadam.financialtracker.entity.TransactionCategory;
import com.buruadam.financialtracker.entity.User;
import com.buruadam.financialtracker.enums.TransactionType;
import com.buruadam.financialtracker.exception.ResourceNotFoundException;
import com.buruadam.financialtracker.repository.AccountRepository;
import com.buruadam.financialtracker.repository.TransactionCategoryRepository;
import com.buruadam.financialtracker.repository.TransactionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionCategoryRepository transactionCategoryRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              TransactionCategoryRepository transactionCategoryRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionCategoryRepository = transactionCategoryRepository;
    }

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {
        User currentUser = getCurrentUser();

        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Account not found with ID: '%s'", request.accountId())
                ));

        if (!account.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You do not have permission to use this account!");
        }

        TransactionCategory category = transactionCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Transaction category not found with ID: '%s'", request.categoryId())
                ));

        if (!category.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You do not have permission to use this category!");
        }

        if (category.getType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance().add(request.amount()));
        } else if (category.getType() == TransactionType.EXPENSE) {
            account.setBalance(account.getBalance().subtract(request.amount()));
        }

        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAmount(request.amount());
        transaction.setDescription(request.description());
        transaction.setDate(request.date());
        transaction.setAccount(account);
        transaction.setCategory(category);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return mapToResponse(savedTransaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByAccount(UUID accountId) {
        User currentUser = getCurrentUser();

        Account account = accountRepository.findById(accountId)
                .filter(acc -> acc.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Account not found with ID: '%s'", accountId)
                ));

        return transactionRepository.findByAccountId(account.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getAllTransactionsForCurrentUser() {
        User currentUser = getCurrentUser();

        return transactionRepository.findByAccountUserId(currentUser.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getDate(),
                transaction.getAccount().getId(),
                transaction.getAccount().getName(),
                transaction.getCategory().getId(),
                transaction.getCategory().getName(),
                transaction.getCategory().getType().name()
        );
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
