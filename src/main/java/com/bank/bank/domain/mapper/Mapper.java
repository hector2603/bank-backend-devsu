package com.bank.bank.domain.mapper;

import com.bank.bank.domain.model.Account;
import com.bank.bank.domain.model.Client;
import com.bank.bank.domain.model.Transaction;
import com.bank.bank.infrastructure.dto.response.AccountResponse;
import com.bank.bank.infrastructure.dto.response.ClientResponse;
import com.bank.bank.infrastructure.dto.response.TransactionResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class Mapper {

    public ClientResponse mapToResponse(Client client){
        return ClientResponse.builder()
                .id(client.getId())
                .name(client.getName())
                .age(client.getAge())
                .address(client.getAddress())
                .gender(client.getGender())
                .phone(client.getPhone())
                .identification(client.getIdentification())
                .status(client.getStatus())
                .accounts(client.getAccounts().stream().map(this::mapToResponse).toList())
                .build();
    }

    public Client converToClientEntity(ClientResponse clientResponse){
        Client client = new Client();
        client.setId(clientResponse.getId());
        client.setName(clientResponse.getName());
        client.setAge(clientResponse.getAge());
        client.setAddress(clientResponse.getAddress());
        client.setGender(clientResponse.getGender());
        client.setPhone(clientResponse.getPhone());
        client.setIdentification(clientResponse.getIdentification());
        client.setStatus(clientResponse.getStatus());
        return client;
    }

    public AccountResponse mapToResponse(Account account){
        List<Transaction> transactions = ObjectUtils.isEmpty(account.getTransactions()) ? List.of() : account.getTransactions();
        account.setTransactions(transactions);
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .initialBalance(account.getInitialBalance())
                .balance(calculateBalance(account))
                .status(account.getStatus())
                .clientId(account.getClient().getId())
                .transactions(transactions.stream().map(this::mapToResponse).toList())
                .build();
    }

    public BigDecimal calculateBalance(Account account){
        BigDecimal balance = account.getInitialBalance();
        BigDecimal total = account.getTransactions().stream()
                .map(Transaction::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return balance.add(total);
    }

    public Account convertToAccountEntity(AccountResponse accountResponse) {
        Account account = new Account();
        account.setId(accountResponse.getId());
        account.setAccountNumber(accountResponse.getAccountNumber());
        account.setAccountType(accountResponse.getAccountType());
        account.setInitialBalance(accountResponse.getInitialBalance());
        account.setStatus(accountResponse.getStatus());
        Client client = new Client();
        client.setId(accountResponse.getClientId());
        account.setClient(client);
        return account;
    }

    public TransactionResponse mapToResponse(Transaction transaction){
        return TransactionResponse.builder()
                .id(transaction.getId())
                .creationDate(transaction.getCreationDate())
                .transactionType(transaction.getTransactionType())
                .value(transaction.getValue())
                .balance(transaction.getBalance())
                .accountNumber(transaction.getAccount().getAccountNumber())
                .build();
    }

}
