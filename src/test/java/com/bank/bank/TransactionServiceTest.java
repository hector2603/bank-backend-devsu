package com.bank.bank;

import com.bank.bank.domain.mapper.Mapper;
import com.bank.bank.domain.model.Transaction;
import com.bank.bank.domain.model.TransactionType;
import com.bank.bank.domain.service.AccountService;
import com.bank.bank.domain.service.impl.TransactionServiceImpl;
import com.bank.bank.infrastructure.dto.request.CreateTransactionRequest;
import com.bank.bank.infrastructure.dto.response.AccountResponse;
import com.bank.bank.infrastructure.dto.response.TransactionResponse;
import com.bank.bank.infrastructure.repository.TransactionRepository;
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
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    public void testCreateTransaction() {
        // Arrange
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setValue(new BigDecimal(100));
        request.setAccountId(1L);
        request.setTransactionType(TransactionType.DEPOSIT);
        Transaction transaction = new Transaction();
        transaction.setBalance(new BigDecimal(100));
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setValue(new BigDecimal(100));
        TransactionResponse response = new TransactionResponse();

        when(accountService.calculateBalance(any())).thenReturn(new BigDecimal(100)  );
        when(accountService.getAccount(any())).thenReturn(new AccountResponse());
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(mapper.mapToResponse(any(Transaction.class))).thenReturn(response);

        // Act
        TransactionResponse result = transactionService.createTransaction(request);

        // Assert
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(mapper, times(1)).mapToResponse(transaction);
        Assertions.assertEquals(response, result);
    }

    @Test
    public void testUpdateTransaction() {
        // Arrange
        Long id = 1L;
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setValue(new BigDecimal(100));
        request.setAccountId(1L);
        request.setTransactionType(TransactionType.DEPOSIT);
        Transaction transaction = new Transaction();
        transaction.setBalance(new BigDecimal(100));
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setValue(new BigDecimal(100));
        TransactionResponse response = new TransactionResponse();

        when(transactionRepository.findById(any())).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(mapper.mapToResponse(any(Transaction.class))).thenReturn(response);

        // Act
        TransactionResponse result = transactionService.updateTransaction(id, request);

        // Assert
        verify(transactionRepository, times(1)).findById(id);
        verify(transactionRepository, times(1)).save(transaction);
        verify(mapper, times(1)).mapToResponse(transaction);
        Assertions.assertEquals(response, result);
    }

    @Test
    public void testGetTransaction() {
        // Arrange
        Long id = 1L;
        Transaction transaction = new Transaction();
        TransactionResponse response = new TransactionResponse();

        when(transactionRepository.findById(any())).thenReturn(Optional.of(transaction));
        when(mapper.mapToResponse(any(Transaction.class))).thenReturn(response);

        // Act
        TransactionResponse result = transactionService.getTransaction(id);

        // Assert
        verify(transactionRepository, times(1)).findById(id);
        verify(mapper, times(1)).mapToResponse(transaction);
        Assertions.assertEquals(response, result);
    }

    @Test
    public void testGetAllTransactions() {
        // Arrange
        Transaction transaction = new Transaction();
        TransactionResponse response = new TransactionResponse();

        when(transactionRepository.findAll()).thenReturn(Collections.singletonList(transaction));
        when(mapper.mapToResponse(any(Transaction.class))).thenReturn(response);

        // Act
        List<TransactionResponse> result = transactionService.getAllTransactions();

        // Assert
        verify(transactionRepository, times(1)).findAll();
        verify(mapper, times(1)).mapToResponse(transaction);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    public void testDeleteTransaction() {
        // Arrange
        Long id = 1L;
        Transaction transaction = new Transaction();
        TransactionResponse response = new TransactionResponse();

        when(transactionRepository.findById(any())).thenReturn(Optional.of(transaction));
        when(mapper.mapToResponse(any(Transaction.class))).thenReturn(response);

        // Act
        transactionService.deleteTransaction(id);

        // Assert
        verify(transactionRepository, times(1)).findById(id);
        verify(transactionRepository, times(1)).deleteById(id);
    }

}
