package com.buruadam.financialtracker.repository;

import com.buruadam.financialtracker.entity.Transaction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @EntityGraph(attributePaths = {"account", "category"})
    List<Transaction> findByAccountUserId(UUID userId);

    @EntityGraph(attributePaths = {"account", "category"})
    List<Transaction> findByAccountId(UUID accountId);

}
