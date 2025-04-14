package com.desafio.picpay_simplificado.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WalletDepositDto(

        @NotNull(message = "Deposit amount cannot be null")
        @DecimalMin(value = "0.01", message = "Deposit must be greater than zero")
        BigDecimal amount
) {
}