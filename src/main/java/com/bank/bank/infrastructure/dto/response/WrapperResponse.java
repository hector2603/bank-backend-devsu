package com.bank.bank.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class WrapperResponse <T>{
    private T data;
    private String error;
}
