package com.bank.bank.domain.service;

import com.bank.bank.infrastructure.dto.request.CreateTransactionRequest;
import com.bank.bank.infrastructure.dto.response.TransactionResponse;

import java.util.List;

public interface TransactionService {
    TransactionResponse createTransaction(CreateTransactionRequest request);
    TransactionResponse updateTransaction(Long id, CreateTransactionRequest request);
    TransactionResponse getTransaction(Long id);
    List<TransactionResponse> getAllTransactions();
    void deleteTransaction(Long id);
}