package com.desafio.picpay_simplificado.web.dto;

import java.time.Instant;

public record TokenResponseDto(
        Long userId,
        String token,
        Instant expiresAt
) {
}