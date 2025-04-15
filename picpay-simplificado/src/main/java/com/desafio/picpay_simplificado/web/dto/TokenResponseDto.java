package com.desafio.picpay_simplificado.web.dto;

import java.time.Instant;
import java.util.UUID;

public record TokenResponseDto(
        UUID userId,
        String token,
        Instant expiresAt
) {
}