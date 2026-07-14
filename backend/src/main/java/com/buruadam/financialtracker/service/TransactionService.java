package com.buruadam.financialtracker.service;

import com.buruadam.financialtracker.dto.TransactionRequest;
import com.buruadam.financialtracker.dto.TransactionResponse;
import com.buruadam.financialtracker.entity.Account;
import com.buruadam.financialtracker.entity.Category;
import com.buruadam.financialtracker.entity.Transaction;
import com.buruadam.financialtracker.enums.TransactionType;
import com.buruadam.financialtracker.exception.ResourceNotFoundException;
import com.buruadam.financialtracker.repository.AccountRepository;
import com.buruadam.financialtracker.repository.CategoryRepository;
import com.buruadam.financialtracker.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request, UUID userId) {


        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (!account.getUser().getId().equals(userId)) {
            throw new RuntimeException("You do not have permission to use this account!");
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!category.getUser().getId().equals(userId)) {
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
    public List<TransactionResponse> getTransactionsByAccount(UUID accountId, UUID userId) {
        Account account = accountRepository.findById(accountId)
                .filter(acc -> acc.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return transactionRepository.findByAccountId(account.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getAllTransactionsForCurrentUser(UUID userId) {
        return transactionRepository.findByAccountUserId(userId).stream()
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
}
