package com.bank.bank.domain.service;

import com.bank.bank.domain.model.Account;
import com.bank.bank.domain.model.Report;
import com.bank.bank.domain.model.Transaction;
import com.bank.bank.domain.model.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
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
    private TransactionService transactionService;

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
            for (Transaction transaction : transactions) {
                if (transaction.getTransactionType() == TransactionType.DEPOSIT) {
                    totalCredits = totalCredits.add(transaction.getValue());
                } else if (transaction.getTransactionType() == TransactionType.WITHDRAWAL) {
                    totalDebits = totalDebits.add(transaction.getValue());
                }
            }
        }

        Report report = new Report();
        report.setAccounts(accounts);
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
            document.add(new Paragraph("Accounts: " + report.getAccounts()));
            document.add(new Paragraph("Total Debits: " + report.getTotalDebits()));
            document.add(new Paragraph("Total Credits: " + report.getTotalCredits()));
            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF report", e);
        }
    }
}
