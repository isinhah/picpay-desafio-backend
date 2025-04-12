package com.desafio.picpay_simplificado.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionRequestDto(

        @NotNull(message = "Transaction amount is required")
        @DecimalMin(value = "0.01", message = "Transaction amount must be greater than zero")
        BigDecimal amount,

        @NotNull(message = "Payer ID is required")
        UUID payerId,

        @NotNull(message = "Payee ID is required")
        UUID payeeId
) {
}