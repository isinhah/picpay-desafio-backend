package com.desafio.picpay_simplificado.web.controller;

import com.desafio.picpay_simplificado.service.WalletService;
import com.desafio.picpay_simplificado.web.dto.WalletDepositDto;
import com.desafio.picpay_simplificado.web.dto.WalletResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users/{userId}/wallet")
public class WalletController {

    private final WalletService walletService;

    @GetMapping
    public ResponseEntity<WalletResponseDto> getWalletById(@PathVariable UUID userId) {
        WalletResponseDto responseDto = walletService.findByUser(userId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/deposit")
    public ResponseEntity<WalletResponseDto> depositToWallet(
            @PathVariable UUID userId,
            @RequestBody @Valid WalletDepositDto depositDto) {
        WalletResponseDto responseDto = walletService.depositToWallet(userId, depositDto);
        return ResponseEntity.ok(responseDto);
    }
}