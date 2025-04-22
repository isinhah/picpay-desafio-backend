package com.desafio.picpay_simplificado.service;

import com.desafio.picpay_simplificado.entity.User;
import com.desafio.picpay_simplificado.repository.UserRepository;
import com.desafio.picpay_simplificado.utils.UserValidator;
import com.desafio.picpay_simplificado.web.dto.UserRequestDto;
import com.desafio.picpay_simplificado.web.dto.UserResponseDto;
import com.desafio.picpay_simplificado.web.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final WalletService walletService;
    private final UserValidator userValidator;

    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
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

        UserRequestDto encodedDto = encodePassword(registerDto);

        User userToSave = UserMapper.INSTANCE.toUser(encodedDto);
        User savedUser = userRepository.save(userToSave);

        walletService.createWallet(savedUser);

        return UserMapper.INSTANCE.toDto(savedUser);
    }

    @Transactional
    public UserResponseDto update(Long id, UserRequestDto updateDto) {
        User existingUser = findUserById(id);

        userValidator.validateUserUpdateEmailAndDocument(id, updateDto.email(), updateDto.document());
        userValidator.validateUserRoleAndDocument(updateDto.role(), updateDto.document());

        UserRequestDto encodedDto = encodePassword(updateDto);

        UserMapper.INSTANCE.updateFromDto(encodedDto, existingUser);

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.INSTANCE.toDto(updatedUser);
    }

    @Transactional
    public void delete(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    private UserRequestDto encodePassword(UserRequestDto dto) {
        String encodedPassword = userValidator.encodePassword(dto.password());
        return new UserRequestDto(
                dto.name(),
                dto.document(),
                dto.email(),
                encodedPassword,
                dto.role()
        );
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id: '%s' not found", id)));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with email: '%s' not found", email)));
    }
}