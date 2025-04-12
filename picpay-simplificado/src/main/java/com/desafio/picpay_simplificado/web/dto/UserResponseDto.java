package com.desafio.picpay_simplificado.web.dto;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String name,
        String email,
        String role
) {
}