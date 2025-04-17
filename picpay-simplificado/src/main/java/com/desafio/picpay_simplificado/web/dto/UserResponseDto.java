package com.desafio.picpay_simplificado.web.dto;

public record UserResponseDto(
        Long id,
        String name,
        String email,
        String role
) {
}