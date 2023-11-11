package com.bank.bank.infrastructure.dto.response;

import com.bank.bank.domain.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    private Long id;
    private LocalDateTime creationDate;
    private TransactionType transactionType;
    private BigDecimal value;
    private BigDecimal balance;
    private Long accountNumber;

}
