package com.buruadam.financialtracker.repository;

import com.buruadam.financialtracker.entity.TransactionCategory;
import com.buruadam.financialtracker.entity.User;
import com.buruadam.financialtracker.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, Long> {
    List<TransactionCategory> findByUser(User user);
    Optional<TransactionCategory> findByUserIdAndNameAndType(UUID userId, String name, TransactionType type);
}
