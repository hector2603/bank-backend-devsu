package com.bank.bank.domain.service;

import com.bank.bank.domain.model.Transaction;
import com.bank.bank.infrastructure.dto.request.CreateTransactionRequest;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(CreateTransactionRequest request);
    Transaction updateTransaction(Long id, CreateTransactionRequest request);
    Transaction getTransaction(Long id);
    List<Transaction> getAllTransactions();
    void deleteTransaction(Long id);
}