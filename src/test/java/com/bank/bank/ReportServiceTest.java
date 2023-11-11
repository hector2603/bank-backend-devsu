package com.bank.bank;

import com.bank.bank.domain.mapper.Mapper;
import com.bank.bank.domain.model.Account;
import com.bank.bank.domain.model.Report;
import com.bank.bank.domain.model.Transaction;
import com.bank.bank.domain.service.AccountService;
import com.bank.bank.domain.service.impl.ReportServiceImpl;
import com.bank.bank.infrastructure.dto.response.AccountResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    public void testGenerateAccountStatementReport() {
        // Arrange
        Long clientId = 1L;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        Account account = new Account();
        Transaction transaction = new Transaction();
        transaction.setValue(new BigDecimal(100));
        transaction.setCreationDate(LocalDate.now().atStartOfDay());
        account.setTransactions(Collections.singletonList(transaction));

        when(accountService.getAccountsByClientId(any())).thenReturn(Collections.singletonList(account));

        // Act
        Report result = reportService.generateAccountStatementReport(clientId, startDate, endDate);

        // Assert
        verify(accountService, times(1)).getAccountsByClientId(clientId);
        Assertions.assertNotNull(result);
    }

    @Test
    public void testGeneratePdfReport() {
        // Arrange
        Report report = new Report();
        report.setAccounts(Collections.singletonList(new AccountResponse()));
        report.setTotalDebits(new BigDecimal(100));
        report.setTotalCredits(new BigDecimal(100));

        // Act
        byte[] result = reportService.generatePdfReport(report);

        // Assert
        Assertions.assertNotNull(result);
    }
}
