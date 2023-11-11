package com.bank.bank.domain.model;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Report {
    private List<Account> accounts;
    private BigDecimal totalDebits;
    private BigDecimal totalCredits;

}