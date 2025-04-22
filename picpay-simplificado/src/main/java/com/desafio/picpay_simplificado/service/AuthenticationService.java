package com.desafio.picpay_simplificado.service;

import com.desafio.picpay_simplificado.entity.User;
import com.desafio.picpay_simplificado.security.JwtTokenService;
import com.desafio.picpay_simplificado.web.dto.LoginRequestDto;
import com.desafio.picpay_simplificado.web.dto.TokenResponseDto;
import com.desafio.picpay_simplificado.web.dto.UserRequestDto;
import com.desafio.picpay_simplificado.web.dto.UserResponseDto;
import com.desafio.picpay_simplificado.web.exception.InvalidPasswordException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final UserService userService;
    private final JwtTokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public TokenResponseDto register(UserRequestDto dto) {
        UserResponseDto createdUserDto = userService.create(dto);
        User createdUser = userService.findUserById(createdUserDto.id());

        String token = tokenService.generateToken(createdUser);
        Instant expiresAt = tokenService.generateExpirationDate();

        return new TokenResponseDto(createdUser.getId(), token, expiresAt);
    }

    public TokenResponseDto login(LoginRequestDto dto) {
        User user = userService.findByEmail(dto.email());

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        String token = tokenService.generateToken(user);
        Instant expiresAt = tokenService.generateExpirationDate();

        return new TokenResponseDto(user.getId(), token, expiresAt);
    }
}