package com.bank.bank.domain.mapper;

import com.bank.bank.domain.model.Account;
import com.bank.bank.domain.model.Client;
import com.bank.bank.domain.service.AccountService;
import com.bank.bank.infrastructure.dto.response.AccountResponse;
import com.bank.bank.infrastructure.dto.response.ClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Mapper {

    @Autowired
    private AccountService accountService;

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
        return client;
    }

    public AccountResponse mapToResponse(Account account) {
        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .initialBalance(account.getInitialBalance())
                .balance(accountService.calculateBalance(account.getId()))
                .status(account.getStatus())
                .clientId(account.getClient().getId())
                .transactions(account.getTransactions())
                .build();
    }

    public Account convertToAccountEntity(AccountResponse accountResponse) {
        Account account = new Account();
        account.setAccountNumber(accountResponse.getAccountNumber());
        account.setAccountType(accountResponse.getAccountType());
        return account;
    }

}
