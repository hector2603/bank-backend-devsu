package com.bank.bank.infrastructure.dto.request;

import com.bank.bank.domain.model.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionRequest {
    @NotNull(message = "Account id is mandatory")
    private Long accountNumber;
    @NotNull(message = "Transaction type is mandatory")
    private TransactionType transactionType;
    @NotNull(message = "Value is mandatory")
    private BigDecimal value;
}