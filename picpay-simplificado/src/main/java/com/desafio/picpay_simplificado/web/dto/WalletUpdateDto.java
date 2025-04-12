package com.desafio.picpay_simplificado.web.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record WalletUpdateDto(
        @Positive(message = "Balance must be positive")
        BigDecimal balance
) {
}