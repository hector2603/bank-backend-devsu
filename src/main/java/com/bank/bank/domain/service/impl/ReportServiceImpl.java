package com.bank.bank.domain.service.impl;

import com.bank.bank.domain.mapper.Mapper;
import com.bank.bank.domain.model.Account;
import com.bank.bank.domain.model.Report;
import com.bank.bank.domain.model.Transaction;
import com.bank.bank.domain.model.TransactionType;
import com.bank.bank.domain.service.AccountService;
import com.bank.bank.domain.service.ReportService;
import com.bank.bank.infrastructure.dto.response.AccountResponse;
import com.itextpdf.text.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private Mapper mapper;

    @Override
    public Report generateAccountStatementReport(Long clientId, LocalDate startDate, LocalDate endDate) {
        List<Account> accounts = accountService.getAccountsByClientId(clientId);
        BigDecimal totalDebits = BigDecimal.ZERO;
        BigDecimal totalCredits = BigDecimal.ZERO;
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        for (Account account : accounts) {
            List<Transaction> transactions = account.getTransactions()
                    .stream()
                    .filter(transaction -> !transaction.getCreationDate().isBefore(startDateTime) && !transaction.getCreationDate().isAfter(endDateTime))
                    .toList();
            account.setTransactions(transactions);
            for (Transaction transaction : transactions) {
                if (transaction.getTransactionType() == TransactionType.DEPOSIT) {
                    totalCredits = totalCredits.add(transaction.getValue());
                } else if (transaction.getTransactionType() == TransactionType.WITHDRAWAL) {
                    totalDebits = totalDebits.add(transaction.getValue());
                }
            }
        }

        Report report = new Report();
        report.setAccounts(accounts.stream().map(mapper::mapToResponse).toList());
        report.setTotalDebits(totalDebits);
        report.setTotalCredits(totalCredits);

        return report;
    }

    @Override
    public byte[] generatePdfReport(Report report) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Define custom fonts
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

            // Add title
            Paragraph title = new Paragraph("Account Statement Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Add a line break
            document.add(Chunk.NEWLINE);

            // Add content
            document.add(new Paragraph("Accounts: ", titleFont));
            for (AccountResponse account : report.getAccounts()) {
                document.add(new Paragraph("Account ID: " + account.getId(), normalFont));
                document.add(new Paragraph("Account Balance: " + account.getBalance(), normalFont));
                document.add(new Paragraph("Account Client ID: " + account.getClientId(), normalFont));
                document.add(Chunk.NEWLINE);
            }
            document.add(new Paragraph("Total Debits: " + report.getTotalDebits(), titleFont));
            document.add(new Paragraph("Total Credits: " + report.getTotalCredits(), titleFont));

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF report", e);
        }
    }
}
