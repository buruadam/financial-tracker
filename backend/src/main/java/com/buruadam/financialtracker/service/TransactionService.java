package com.buruadam.financialtracker.service;

import com.buruadam.financialtracker.dto.transaction.TransactionCreateRequest;
import com.buruadam.financialtracker.dto.transaction.TransactionResponseDto;
import com.buruadam.financialtracker.entity.Account;
import com.buruadam.financialtracker.entity.Category;
import com.buruadam.financialtracker.entity.Transaction;
import com.buruadam.financialtracker.exception.ResourceNotFoundException;
import com.buruadam.financialtracker.exception.UnauthorizedAccessException;
import com.buruadam.financialtracker.mapper.TransactionMapper;
import com.buruadam.financialtracker.repository.AccountRepository;
import com.buruadam.financialtracker.repository.CategoryRepository;
import com.buruadam.financialtracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    public TransactionResponseDto createTransaction(TransactionCreateRequest request, UUID userId) {
        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        if (!account.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not own this account");
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (!category.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not own this category");
        }

        Transaction transaction = transactionMapper.toEntity(request);
        transaction.setAccount(account);
        transaction.setCategory(category);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return transactionMapper.toResponseDto(savedTransaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getMyTransactions(UUID userId) {
        List<Transaction> transactions = transactionRepository.findByAccountUserId(userId);

        return transactions.stream()
                .map(transactionMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByAccount(UUID accountId, UUID userId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (!account.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not have access to this account");
        }

        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);

        return transactions.stream()
                .map(transactionMapper::toResponseDto)
                .toList();
    }

}
