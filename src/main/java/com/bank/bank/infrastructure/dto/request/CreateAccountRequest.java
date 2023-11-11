package com.bank.bank.infrastructure.dto.request;


import com.bank.bank.domain.model.AccountType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {

    @NotNull(message = "Client ID is mandatory")
    private Long clientId;

    @NotNull(message = "Account type is mandatory")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @NotNull(message = "Initial balance is mandatory")
    @Positive(message = "Initial balance must be a positive number")
    private BigDecimal initialBalance;

}
