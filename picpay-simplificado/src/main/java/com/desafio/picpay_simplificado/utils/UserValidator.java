package com.desafio.picpay_simplificado.utils;

import com.desafio.picpay_simplificado.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserValidator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void validateUserRoleAndDocument(String role, String document) {
        if (role.equalsIgnoreCase("USER") && document.length() != 11) {
            throw new IllegalArgumentException("USER must have a CPF with exactly 11 digits.");
        }

        if (role.equalsIgnoreCase("MERCHANT") && document.length() != 14) {
            throw new IllegalArgumentException("MERCHANT must have a CNPJ with exactly 14 digits.");
        }
    }

    public void validateUserEmailAndDocument(String email, String document) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("This email is already registered.");
        }

        if (userRepository.existsByDocument(document)) {
            throw new IllegalArgumentException("This document (CPF/CNPJ) is already registered.");
        }
    }

    public void validateUserUpdateEmailAndDocument(Long userId, String email, String document) {
        userRepository.findByEmail(email).ifPresent(user -> {
            if (!user.getId().equals(userId)) {
                throw new IllegalArgumentException("This email is already registered to another user.");
            }
        });

        userRepository.findByDocument(document).ifPresent(user -> {
            if (!user.getId().equals(userId)) {
                throw new IllegalArgumentException("This document (CPF/CNPJ) is already registered to another user.");
            }
        });
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}