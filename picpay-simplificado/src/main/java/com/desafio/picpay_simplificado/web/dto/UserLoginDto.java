package com.desafio.picpay_simplificado.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginDto(
        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email format is invalid", regexp = "^[a-z0-9.+-_]+@[a-z0-9.-]+\\.[a-z]{2,}$")
        @Size(max = 100, message = "Email must be at most 100 characters long")
        String email,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, max = 8, message = "Password size must be exactly 8 characters")
        String password
) {
}
