package com.desafio.picpay_simplificado.web.controller;

import com.desafio.picpay_simplificado.service.AuthenticationService;
import com.desafio.picpay_simplificado.web.dto.LoginRequestDto;
import com.desafio.picpay_simplificado.web.dto.TokenResponseDto;
import com.desafio.picpay_simplificado.web.dto.UserRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> register(@RequestBody @Valid UserRequestDto registerDto) {
        TokenResponseDto tokenResponse = authenticationService.register(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginRequestDto loginDto) {
        TokenResponseDto tokenResponse = authenticationService.login(loginDto);
        return ResponseEntity.ok(tokenResponse);
    }
}