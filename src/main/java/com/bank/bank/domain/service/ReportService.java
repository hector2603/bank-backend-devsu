package com.bank.bank.domain.service;
import com.bank.bank.domain.model.Report;

import java.time.LocalDate;

public interface ReportService {
    Report generateAccountStatementReport(Long clientId, LocalDate startDate, LocalDate endDate);

    byte[] generatePdfReport(Report report);
}
