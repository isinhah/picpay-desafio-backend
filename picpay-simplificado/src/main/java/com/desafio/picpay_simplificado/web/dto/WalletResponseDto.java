package com.desafio.picpay_simplificado.web.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletResponseDto(
        UUID id,
        UUID userId,
        BigDecimal balance
) {
}