package com.desafio.picpay_simplificado.web.controller;

import com.desafio.picpay_simplificado.service.WalletService;
import com.desafio.picpay_simplificado.web.dto.WalletDepositDto;
import com.desafio.picpay_simplificado.web.dto.WalletResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users/{userId}/wallet")
public class WalletController {

    private final WalletService walletService;

    @PreAuthorize("#userId == authentication.principal or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<WalletResponseDto> getWalletById(@PathVariable Long userId) {
        WalletResponseDto responseDto = walletService.findByUser(userId);
        return ResponseEntity.ok(responseDto);
    }

    @PreAuthorize("#userId == authentication.principal")
    @PostMapping("/deposit")
    public ResponseEntity<WalletResponseDto> depositToWallet(
            @PathVariable Long userId,
            @RequestBody @Valid WalletDepositDto depositDto) {
        WalletResponseDto responseDto = walletService.depositToWallet(userId, depositDto);
        return ResponseEntity.ok(responseDto);
    }
}