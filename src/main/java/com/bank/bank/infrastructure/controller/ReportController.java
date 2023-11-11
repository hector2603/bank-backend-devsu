package com.bank.bank.infrastructure.controller;

import com.bank.bank.domain.model.Report;
import com.bank.bank.domain.model.ReportType;
import com.bank.bank.domain.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/reports")
@Tag(name = "Report Controller", description = "Controller to manage the reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    @Operation(summary = "Get Account Statement Report", description = "This method is used to get the account statement report")
    public ResponseEntity<?> getAccountStatementReport(@RequestParam Long clientId,
                                                       @RequestParam String startDate,
                                                       @RequestParam String endDate,
                                                       @RequestParam ReportType reportType) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        Report report = reportService.generateAccountStatementReport(clientId, start, end);

        if (reportType == ReportType.PDF) {
            byte[] pdfReport = reportService.generatePdfReport(report);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfReport);
        } else {
            return ResponseEntity.ok(report);
        }
    }
}