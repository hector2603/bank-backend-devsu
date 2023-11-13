package com.bank.bank.domain.service.impl;

import com.bank.bank.application.exception.NotFoundException;
import com.bank.bank.domain.mapper.Mapper;
import com.bank.bank.domain.model.Transaction;
import com.bank.bank.domain.model.TransactionType;
import com.bank.bank.domain.service.AccountService;
import com.bank.bank.domain.service.TransactionService;
import com.bank.bank.infrastructure.dto.request.CreateTransactionRequest;
import com.bank.bank.infrastructure.dto.response.AccountResponse;
import com.bank.bank.infrastructure.dto.response.TransactionResponse;
import com.bank.bank.infrastructure.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Value("${daily.withdrawal.limit}")
    private BigDecimal dailyWithdrawalLimit;

    @Override
    public TransactionResponse createTransaction(CreateTransactionRequest request) {
        return Optional.of(request)
                .map(req -> accountService.getAccount(req.getAccountNumber()))
                .map(account -> validateTransaction(request, account))
                .map(account -> createTransactionData(request, account))
                .map(transactionRepository::save)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new IllegalArgumentException("Invalid request"));
    }


    @Override
    public TransactionResponse updateTransaction(Long id, CreateTransactionRequest request) {
        return Optional.of(request)
                .map(req -> transactionRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Transaction with id " + id + " not found")))
                .map(transaction -> {
                    AccountResponse account = accountService.getAccount(request.getAccountNumber());
                    validateTransaction(request, account);
                    transaction.setAccount(mapper.convertToAccountEntity(account));
                    transaction.setTransactionType(request.getTransactionType());
                    BigDecimal balance = transaction.getValue().abs().add(request.getValue().abs());
                    balance = request.getTransactionType() == TransactionType.WITHDRAWAL ? balance.negate() : balance;
                    transaction.setBalance(transaction.getBalance().add(balance));
                    transaction.setValue(request.getValue());
                    return transaction;
                })
                .map(transactionRepository::save)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new IllegalArgumentException("Invalid request"));
    }

    @Override
    public TransactionResponse getTransaction(Long id) {
        return transactionRepository.findById(id)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Transaction with id " + id + " not found"));
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {
        return ((List<Transaction>) transactionRepository.findAll())
                .stream()
                .map(mapper::mapToResponse)
                .toList();
    }

    @Override
    public void deleteTransaction(Long id) {
        getTransaction(id);
        transactionRepository.deleteById(id);
    }

    private Transaction createTransactionData(CreateTransactionRequest request, AccountResponse account) {
        BigDecimal balance = accountService.calculateBalance(account.getAccountNumber());
        Transaction transaction = new Transaction();
        transaction.setCreationDate(LocalDateTime.now());
        transaction.setAccount(mapper.convertToAccountEntity(account));
        transaction.setTransactionType(request.getTransactionType());
        transaction.setValue(request.getValue());
        transaction.setBalance(balance.add(request.getValue()));
        return transaction;
    }

    private AccountResponse validateTransaction(CreateTransactionRequest request, AccountResponse account) {
        if(request.getValue().compareTo(BigDecimal.ZERO) == 0){
            throw new IllegalArgumentException("Invalid value, value must be different than zero");
        } else if (request.getTransactionType() == TransactionType.DEPOSIT && request.getValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid value, Deposit value must be greater than zero");
        } else if (request.getTransactionType() == TransactionType.WITHDRAWAL) {
            request.setValue(request.getValue().compareTo(BigDecimal.ZERO) < 0 ? request.getValue() : request.getValue().negate());
            BigDecimal balance = accountService.calculateBalance(account.getAccountNumber());
            if (balance.compareTo(request.getValue().abs()) < 0) {
                throw new IllegalArgumentException("Insufficient balance");
            }
            if (request.getValue().abs().compareTo(dailyWithdrawalLimit) > 0) {
                throw new IllegalArgumentException("Withdrawal limit exceeded");
            }
            BigDecimal totalWithdrawalToday = transactionRepository.getTotalAmountByAccountIdAndTransactionTypeAndDate(account.getAccountNumber(), TransactionType.WITHDRAWAL, LocalDateTime.now() );
            if (!ObjectUtils.isEmpty(totalWithdrawalToday) &&  totalWithdrawalToday.add(request.getValue()).abs().compareTo(dailyWithdrawalLimit) > 0) {
                throw new IllegalArgumentException("Daily withdrawal limit exceeded");
            }
        }
        return account;
    }


}