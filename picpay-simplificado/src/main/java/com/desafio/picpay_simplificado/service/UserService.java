package com.desafio.picpay_simplificado.service;

import com.desafio.picpay_simplificado.entity.User;
import com.desafio.picpay_simplificado.entity.Wallet;
import com.desafio.picpay_simplificado.repository.UserRepository;
import com.desafio.picpay_simplificado.repository.WalletRepository;
import com.desafio.picpay_simplificado.web.dto.UserRequestDto;
import com.desafio.picpay_simplificado.web.dto.UserResponseDto;
import com.desafio.picpay_simplificado.web.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    @Transactional(readOnly = true)
    public UserResponseDto findById(UUID id) {
        User user = findUserById(id);
        return UserMapper.INSTANCE.toDto(user);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserMapper.INSTANCE::toDto);
    }

    @Transactional
    public UserResponseDto create(UserRequestDto registerDto) {
        validateUserEmailAndDocument(registerDto.email(), registerDto.document());
        validateUserRoleAndDocument(registerDto.role(), registerDto.document());

        User userToSave = UserMapper.INSTANCE.toUser(registerDto);
        User savedUser = userRepository.save(userToSave);

        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        walletRepository.save(wallet);

        return UserMapper.INSTANCE.toDto(savedUser);
    }

    @Transactional
    public UserResponseDto update(UUID id, UserRequestDto updateDto) {
        User existingUser = findUserById(id);
        validateUserUpdateEmailAndDocument(id, updateDto.email(), updateDto.document());
        validateUserRoleAndDocument(updateDto.role(), updateDto.document());

        UserMapper.INSTANCE.updateFromDto(updateDto, existingUser);

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.INSTANCE.toDto(updatedUser);
    }

    @Transactional
    public void delete(UUID id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    public void validateUserRoleAndDocument(String role, String document) {
        if (role.equalsIgnoreCase("USER") && document.length() != 11) {
            throw new IllegalArgumentException("USER must have a CPF with exactly 11 digits");
        }

        if (role.equalsIgnoreCase("MERCHANT") && document.length() != 14) {
            throw new IllegalArgumentException("MERCHANT must have a CNPJ with exactly 14 digits");
        }
    }

    public void validateUserEmailAndDocument(String email, String document) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("This email is already registered.");
        }

        if (userRepository.existsByDocument(document)) {
            throw new IllegalArgumentException("This document (cpf/cnpj) is already registered.");
        }
    }

    public void validateUserUpdateEmailAndDocument(UUID userId, String email, String document) {
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

    @Transactional(readOnly = true)
    private User findUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id: '%s' not found", id)));
    }
}