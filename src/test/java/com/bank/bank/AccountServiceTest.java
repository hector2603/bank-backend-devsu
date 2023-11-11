package com.bank.bank;

import com.bank.bank.domain.mapper.Mapper;
import com.bank.bank.domain.model.Account;
import com.bank.bank.domain.model.Client;
import com.bank.bank.domain.service.ClientService;
import com.bank.bank.domain.service.impl.AccountServiceImpl;
import com.bank.bank.infrastructure.dto.request.CreateAccountRequest;
import com.bank.bank.infrastructure.dto.request.UpdateAccountRequest;
import com.bank.bank.infrastructure.dto.response.AccountResponse;
import com.bank.bank.infrastructure.dto.response.ClientResponse;
import com.bank.bank.infrastructure.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    public void testCreateAccount() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest();
        Account account = new Account();
        AccountResponse response = new AccountResponse();
        Client client = new Client();
        client.setStatus(true);

        when(mapper.converToClientEntity(any())).thenReturn(client);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(mapper.mapToResponse(any(Account.class))).thenReturn(response);

        // Act
        AccountResponse result = accountService.createAccount(request);

        // Assert
        verify(accountRepository, times(1)).save(any(Account.class));
        verify(mapper, times(1)).mapToResponse(account);
        Assertions.assertNotNull(result);
    }



    @Test
    public void testUpdateAccount() {
        // Arrange
        UpdateAccountRequest request = new UpdateAccountRequest();
        Account account = new Account();
        AccountResponse response = new AccountResponse();

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(mapper.mapToResponse(any(Account.class))).thenReturn(response);

        // Act
        AccountResponse result = accountService.updateAccount(request);

        // Assert
        verify(accountRepository, times(1)).findByAccountNumber(any());
        verify(accountRepository, times(1)).save(any(Account.class));
        verify(mapper, times(1)).mapToResponse(account);
        Assertions.assertNotNull(result);
    }

    @Test
    public void testGetAccount() {
        // Arrange
        Long accountNumber = 1L;
        Account account = new Account();
        AccountResponse response = new AccountResponse();

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));
        when(mapper.mapToResponse(any(Account.class))).thenReturn(response);

        // Act
        AccountResponse result = accountService.getAccount(accountNumber);

        // Assert
        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
        verify(mapper, times(1)).mapToResponse(account);
        Assertions.assertNotNull(result);
    }

    @Test
    public void testGetAllAccounts() {
        // Arrange
        Account account = new Account();
        AccountResponse response = new AccountResponse();

        when(accountRepository.findAll()).thenReturn(Collections.singletonList(account));
        when(mapper.mapToResponse(any(Account.class))).thenReturn(response);

        // Act
        List<AccountResponse> result = accountService.getAllAccounts();

        // Assert
        verify(accountRepository, times(1)).findAll();
        verify(mapper, times(1)).mapToResponse(account);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    public void testDeleteAccount() {
        // Arrange
        Long accountNumber = 1L;
        Account account = new Account();

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // Act
        accountService.deleteAccount(accountNumber);

        // Assert
        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
        verify(accountRepository, times(1)).save(account);

    }

    @Test
    public void testCalculateBalance() {
        // Arrange
        Long accountId = 1L;
        Account account = new Account();
        account.setInitialBalance(BigDecimal.ZERO);
        account.setTransactions(Collections.emptyList());

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));

        // Act
        BigDecimal result = accountService.calculateBalance(accountId);

        // Assert
        verify(accountRepository, times(1)).findByAccountNumber(accountId);
        Assertions.assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    public void testGetAccountsByClientId() {
        // Arrange
        Long clientId = 1L;
        Account account = new Account();
        ClientResponse clientResponse = new ClientResponse();

        when(clientService.getClient(any())).thenReturn(clientResponse);
        when(accountRepository.findByClientId(any())).thenReturn(Collections.singletonList(account));

        // Act
        List<Account> result = accountService.getAccountsByClientId(clientId);

        // Assert
        verify(clientService, times(1)).getClient(clientId);
        verify(accountRepository, times(1)).findByClientId(any());
        Assertions.assertEquals(1, result.size());
    }
}
