package com.bank.bank.infrastructure.repository;

import com.bank.bank.domain.model.Transaction;
import com.bank.bank.domain.model.TransactionType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    @Query("SELECT SUM(t.value) FROM Transaction t WHERE t.account.id = :accountId AND t.transactionType = :transactionType AND t.creationDate = :date")
    BigDecimal getTotalAmountByAccountIdAndTransactionTypeAndDate(Long accountId, TransactionType transactionType, LocalDateTime date);
}