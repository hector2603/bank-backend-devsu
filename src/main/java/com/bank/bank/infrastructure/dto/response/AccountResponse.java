package com.bank.bank.infrastructure.dto.response;

import com.bank.bank.domain.model.AccountType;
import com.bank.bank.domain.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class AccountResponse {
    private Long accountNumber;
    private AccountType accountType;
    private BigDecimal initialBalance;
    private BigDecimal balance;
    private Boolean status;
    private Long clientId;
    private List<Transaction> transactions;
}
