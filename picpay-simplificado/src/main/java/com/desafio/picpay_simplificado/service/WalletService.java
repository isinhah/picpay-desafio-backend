package com.desafio.picpay_simplificado.service;

import com.desafio.picpay_simplificado.entity.Wallet;
import com.desafio.picpay_simplificado.repository.UserRepository;
import com.desafio.picpay_simplificado.repository.WalletRepository;
import com.desafio.picpay_simplificado.web.dto.WalletDepositDto;
import com.desafio.picpay_simplificado.web.dto.WalletResponseDto;
import com.desafio.picpay_simplificado.web.exception.WalletNotFoundException;
import com.desafio.picpay_simplificado.web.mapper.WalletMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public WalletResponseDto findByUser(UUID userId) {
        Wallet wallet = findWalletByUserId(userId);
        return WalletMapper.INSTANCE.toDto(wallet);
    }

    @Transactional
    public WalletResponseDto depositToWallet(UUID userId, WalletDepositDto depositDto) {
        findUserById(userId);
        Wallet wallet = findWalletByUserId(userId);

        wallet.setBalance(wallet.getBalance().add(depositDto.amount()));

        Wallet updatedWallet = walletRepository.save(wallet);
        return WalletMapper.INSTANCE.toDto(updatedWallet);
    }

    private Wallet findWalletByUserId(UUID userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for user with id: " + userId));
    }

    private void findUserById(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }
}