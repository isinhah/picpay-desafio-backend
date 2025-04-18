package com.desafio.picpay_simplificado.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletResponseDto(
        UUID id,
        BigDecimal balance
) {
}