package com.bank.bank.domain.model;


import com.bank.bank.infrastructure.dto.response.AccountResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Report {
    private List<AccountResponse> accounts;
    private BigDecimal totalDebits;
    private BigDecimal totalCredits;

}