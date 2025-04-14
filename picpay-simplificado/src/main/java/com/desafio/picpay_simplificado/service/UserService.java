package com.desafio.picpay_simplificado.service;

import com.desafio.picpay_simplificado.entity.User;
import com.desafio.picpay_simplificado.entity.Wallet;
import com.desafio.picpay_simplificado.repository.UserRepository;
import com.desafio.picpay_simplificado.repository.WalletRepository;
import com.desafio.picpay_simplificado.utils.UserValidator;
import com.desafio.picpay_simplificado.web.dto.UserRequestDto;
import com.desafio.picpay_simplificado.web.dto.UserResponseDto;
import com.desafio.picpay_simplificado.web.mapper.UserMapper;
import com.desafio.picpay_simplificado.web.mapper.WalletMapper;
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
    private final UserValidator userValidator;

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
        userValidator.validateUserEmailAndDocument(registerDto.email(), registerDto.document());
        userValidator.validateUserRoleAndDocument(registerDto.role(), registerDto.document());

        User userToSave = UserMapper.INSTANCE.toUser(registerDto);
        User savedUser = userRepository.save(userToSave);

        Wallet wallet = WalletMapper.INSTANCE.createWallet(savedUser);
        walletRepository.save(wallet);

        return UserMapper.INSTANCE.toDto(savedUser);
    }

    @Transactional
    public UserResponseDto update(UUID id, UserRequestDto updateDto) {
        User existingUser = findUserById(id);
        userValidator.validateUserUpdateEmailAndDocument(id, updateDto.email(), updateDto.document());
        userValidator.validateUserRoleAndDocument(updateDto.role(), updateDto.document());

        UserMapper.INSTANCE.updateFromDto(updateDto, existingUser);

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.INSTANCE.toDto(updatedUser);
    }

    @Transactional
    public void delete(UUID id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    private User findUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id: '%s' not found", id)));
    }
}