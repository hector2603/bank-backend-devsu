package com.bank.bank.infrastructure.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountRequest {
    @NotNull(message = "Status is mandatory")
    private Boolean status;
    @NotNull(message = "Account number is mandatory")
    private Long accountNumber;
}
