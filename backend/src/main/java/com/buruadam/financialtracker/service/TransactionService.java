package com.buruadam.financialtracker.service;

import com.buruadam.financialtracker.dto.transaction.TransactionCreateRequest;
import com.buruadam.financialtracker.dto.transaction.TransactionResponseDto;
import com.buruadam.financialtracker.entity.Account;
import com.buruadam.financialtracker.entity.Category;
import com.buruadam.financialtracker.entity.Transaction;
import com.buruadam.financialtracker.enums.TransactionType;
import com.buruadam.financialtracker.exception.ResourceNotFoundException;
import com.buruadam.financialtracker.exception.UnauthorizedAccessException;
import com.buruadam.financialtracker.mapper.TransactionMapper;
import com.buruadam.financialtracker.repository.AccountRepository;
import com.buruadam.financialtracker.repository.CategoryRepository;
import com.buruadam.financialtracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        Account account = getValidatedAccount(request.accountId(), userId);
        Category category = getValidatedCategory(request.categoryId(), userId);

        updateAccountBalance(account, request.amount(), category.getType());

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

    @Transactional
    public void deleteTransaction(UUID transactionId, UUID userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        Account account = getValidatedAccount(transaction.getAccount().getId(), userId);

        BigDecimal reverseAmount = transaction.getAmount().negate();
        updateAccountBalance(account, reverseAmount, transaction.getCategory().getType());

        transactionRepository.delete(transaction);
    }

    private Account getValidatedAccount(UUID accountId, UUID userId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        if (!account.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not own this account");
        }
        return account;
    }

    private Category getValidatedCategory(UUID categoryId, UUID userId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (!category.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not own this category");
        }
        return category;
    }

    private void updateAccountBalance(Account account, BigDecimal amount, TransactionType type) {
        if (type == TransactionType.INCOME) {
            account.setBalance(account.getBalance().add(amount));
        } else if (type == TransactionType.EXPENSE) {
            account.setBalance(account.getBalance().subtract(amount));
        }
    }

}
