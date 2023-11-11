package com.bank.bank.infrastructure.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateClientRequest {
    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Gender is mandatory")
    private String gender;

    @NotNull(message = "Age is mandatory")
    private Integer age;

    @NotBlank(message = "Identification is mandatory")
    private String identification;

    @NotBlank(message = "Address is mandatory")
    private String address;

    @NotBlank(message = "Phone is mandatory")
    private String phone;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotNull(message = "Status is mandatory")
    private Boolean status;
}
