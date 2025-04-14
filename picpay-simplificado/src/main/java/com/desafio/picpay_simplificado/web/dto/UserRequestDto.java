package com.desafio.picpay_simplificado.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDto(

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 120, message = "Name must be at most 120 characters long")
    String name,

    @NotBlank(message = "Document cannot be empty")
    @Size(max = 18, message = "Document (CPF or CNPJ) must be at most 18 characters long")
    String document,

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email format is invalid", regexp = "^[a-z0-9.+-_]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    @Size(max = 100, message = "Email must be at most 100 characters long")
    String email,

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 8, message = "Password size must be exactly 8 characters")
    String password,

    @NotBlank(message = "Role cannot be empty. Choose 'USER' or 'MERCHANT")
    String role
) {
}