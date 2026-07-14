package com.buruadam.financialtracker.repository;

import com.buruadam.financialtracker.entity.Category;
import com.buruadam.financialtracker.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByUserId(UUID userId);
    boolean existsByUserIdAndNameAndType(UUID userId, String name, TransactionType type);
}
