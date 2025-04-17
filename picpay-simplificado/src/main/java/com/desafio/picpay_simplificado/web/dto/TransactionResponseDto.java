package com.desafio.picpay_simplificado.web.dto;

import java.math.BigDecimal;

public record TransactionResponseDto(
        BigDecimal amount,
        Long payer,
        Long payee
) {
}