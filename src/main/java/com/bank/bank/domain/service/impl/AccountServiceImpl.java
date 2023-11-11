package com.bank.bank.domain.service.impl;

import com.bank.bank.application.exception.NotFoundException;
import com.bank.bank.domain.mapper.Mapper;
import com.bank.bank.domain.model.Account;
import com.bank.bank.domain.model.Client;
import com.bank.bank.domain.model.Transaction;
import com.bank.bank.domain.model.TransactionType;
import com.bank.bank.domain.service.AccountService;
import com.bank.bank.domain.service.ClientService;
import com.bank.bank.infrastructure.dto.request.CreateAccountRequest;
import com.bank.bank.infrastructure.dto.request.UpdateAccountRequest;
import com.bank.bank.infrastructure.dto.response.AccountResponse;
import com.bank.bank.infrastructure.dto.response.ClientResponse;
import com.bank.bank.infrastructure.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private Mapper mapper;

    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {
        return Optional.of(request)
                .map(account -> createAccountDetails(new Account(), account))
                .map(this::validateActiveClient)
                .map(accountRepository::save)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new IllegalArgumentException("Invalid request"));
    }

    @Override
    public AccountResponse updateAccount( UpdateAccountRequest request) {
        return Optional.of(request)
                .map(account -> accountRepository.findByAccountNumber(request.getAccountNumber())
                        .orElseThrow(() -> new NotFoundException("Account with accountNumber " + request.getAccountNumber() + " not found")))
                .map(account -> {
                    account.setStatus(request.getStatus());
                    return account;
                })
                .map(accountRepository::save)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new IllegalArgumentException("Invalid request"));
    }

    @Override
    public AccountResponse getAccount(Long accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Account with accountNumber " + accountNumber + " not found"));
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(mapper::mapToResponse)
                .toList();
    }

    @Override
    public void deleteAccount(Long accountNumber) {
        accountRepository.findByAccountNumber(accountNumber)
                .map(account -> {
                    account.setStatus(false);
                    return account;
                })
                .map(accountRepository::save)
                .orElseThrow(() -> new NotFoundException("Account with accountNumber " + accountNumber + " not found"));
    }

    @Override
    public BigDecimal calculateBalance(Long accountId) {
        Account account = accountRepository.findByAccountNumber(accountId).orElseThrow(() -> new NotFoundException("Account with accountNumber " + accountId + " not found"));
        BigDecimal balance = account.getInitialBalance();
        for (Transaction transaction : account.getTransactions()) {
            if (transaction.getTransactionType() == TransactionType.DEPOSIT) {
                balance = balance.add(transaction.getValue());
            } else if (transaction.getTransactionType() == TransactionType.WITHDRAWAL) {
                balance = balance.subtract(transaction.getValue());
            }
        }
        return balance;
    }

    @Override
    public List<Account> getAccountsByClientId(Long clientId) {
        ClientResponse client = clientService.getClient(clientId);
        return accountRepository.findByClientId(client.getId());
    }

    private Account createAccountDetails(Account account, CreateAccountRequest request) {
        account.setStatus(true);
        account.setAccountType(request.getAccountType());
        account.setAccountNumber(generateAccountNumber());
        account.setInitialBalance(request.getInitialBalance());
        account.setClient(mapper.converToClientEntity(clientService.getClient(request.getClientId())));
        return account;
    }



    private Account validateActiveClient(Account account){
        if (!account.getClient().getStatus()) {
            throw new IllegalArgumentException("Client is not active");
        }
        return account;
    }


    private Long generateAccountNumber() {
        Long accountNumber;
        do {
            accountNumber = 1_000_000_000L + (long) (Math.random() * 9_000_000_000L);
        } while (accountRepository.findByAccountNumber(accountNumber).isPresent());
        return accountNumber;
    }

}
