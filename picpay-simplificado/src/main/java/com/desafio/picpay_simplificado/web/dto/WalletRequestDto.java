package com.desafio.picpay_simplificado.web.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record WalletRequestDto(
        @NotNull(message = "User ID cannot be null")
        UUID userId
) {
}
