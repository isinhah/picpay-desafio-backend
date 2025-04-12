package com.desafio.picpay_simplificado.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionResponseDto(
        Long id,
        BigDecimal amount,
        UUID payer,
        UUID payee
) {
}