package com.bank.bank.domain.service;

import com.bank.bank.domain.model.Account;
import com.bank.bank.domain.model.Client;
import com.bank.bank.infrastructure.dto.request.CreateAccountRequest;
import com.bank.bank.infrastructure.dto.request.CreateClientRequest;
import com.bank.bank.infrastructure.dto.request.UpdateAccountRequest;
import com.bank.bank.infrastructure.dto.response.AccountResponse;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    AccountResponse createAccount(CreateAccountRequest request);

    AccountResponse updateAccount(UpdateAccountRequest request);

    AccountResponse getAccount(Long accountNumber);
    List<AccountResponse> getAllAccounts();
    void deleteAccount(Long accountNumber);
    BigDecimal calculateBalance(Long accountId);
    List<Account> getAccountsByClientId(Long clientId);
}
