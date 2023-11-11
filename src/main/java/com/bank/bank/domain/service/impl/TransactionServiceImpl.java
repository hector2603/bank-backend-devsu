package com.bank.bank.domain.service.impl;

import com.bank.bank.application.exception.NotFoundException;
import com.bank.bank.domain.mapper.Mapper;
import com.bank.bank.domain.model.Transaction;
import com.bank.bank.domain.model.TransactionType;
import com.bank.bank.domain.service.AccountService;
import com.bank.bank.domain.service.TransactionService;
import com.bank.bank.infrastructure.dto.request.CreateTransactionRequest;
import com.bank.bank.infrastructure.dto.response.AccountResponse;
import com.bank.bank.infrastructure.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private Mapper mapper;

    @Override
    public Transaction createTransaction(CreateTransactionRequest request) {
        return Optional.of(request)
                .map(req -> accountService.getAccount(req.getAccountId()))
                .map(account -> validateTransaction(request, account))
                .map(account -> createTransactionData(request, account))
                .map(transactionRepository::save)
                .orElseThrow(() -> new IllegalArgumentException("Invalid request"));
    }


    @Override
    public Transaction updateTransaction(Long id, CreateTransactionRequest request) {
        return Optional.of(request)
                .map(req -> getTransaction(id))
                .map(transaction -> {
                    AccountResponse account = accountService.getAccount(request.getAccountId());
                    validateTransaction(request, account);
                    transaction.setAccount(mapper.convertToAccountEntity(account));
                    transaction.setTransactionType(request.getTransactionType());
                    transaction.setValue(request.getValue());
                    return transaction;
                })
                .map(transactionRepository::save)
                .orElseThrow(() -> new IllegalArgumentException("Invalid request"));
    }

    @Override
    public Transaction getTransaction(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction with id " + id + " not found"));
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return (List<Transaction>) transactionRepository.findAll();
    }

    @Override
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    private Transaction createTransactionData(CreateTransactionRequest request, AccountResponse account) {
        Transaction transaction = new Transaction();
        transaction.setAccount(mapper.convertToAccountEntity(account));
        transaction.setTransactionType(request.getTransactionType());
        transaction.setValue(request.getValue());
        return transaction;
    }

    private AccountResponse validateTransaction(CreateTransactionRequest request, AccountResponse account) {
        if (request.getTransactionType() == TransactionType.WITHDRAWAL) {
            BigDecimal balance = accountService.calculateBalance(account.getAccountNumber());
            if (balance.compareTo(request.getValue()) < 0) {
                throw new IllegalArgumentException("Insufficient balance");
            }
            if (request.getValue().compareTo(new BigDecimal(1000)) > 0) {
                throw new IllegalArgumentException("Withdrawal limit exceeded");
            }
            BigDecimal totalWithdrawalToday = transactionRepository.getTotalAmountByAccountIdAndTransactionTypeAndDate(account.getAccountNumber(), TransactionType.WITHDRAWAL, LocalDate.now() );
            if (totalWithdrawalToday.add(request.getValue()).compareTo(new BigDecimal(1000)) > 0) {
                throw new IllegalArgumentException("Daily withdrawal limit exceeded");
            }
        }
        return account;
    }


}