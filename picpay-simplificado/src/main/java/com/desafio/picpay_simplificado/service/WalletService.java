package com.desafio.picpay_simplificado.service;

import com.desafio.picpay_simplificado.entity.User;
import com.desafio.picpay_simplificado.entity.Wallet;
import com.desafio.picpay_simplificado.repository.WalletRepository;
import com.desafio.picpay_simplificado.web.dto.WalletDepositDto;
import com.desafio.picpay_simplificado.web.dto.WalletResponseDto;
import com.desafio.picpay_simplificado.web.exception.WalletNotFoundException;
import com.desafio.picpay_simplificado.web.mapper.WalletMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class WalletService {

    private final WalletRepository walletRepository;

    @Transactional(readOnly = true)
    public WalletResponseDto findByUser(Long userId) {
        Wallet wallet = findWalletByUserId(userId);
        return WalletMapper.INSTANCE.toDto(wallet);
    }

    public void createWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(BigDecimal.ZERO);
        walletRepository.save(wallet);
    }

    @Transactional
    public WalletResponseDto depositToWallet(Long userId, WalletDepositDto depositDto) {
        Wallet wallet = findWalletByUserId(userId);
        wallet.setBalance(wallet.getBalance().add(depositDto.amount()));

        Wallet updatedWallet = walletRepository.save(wallet);
        return WalletMapper.INSTANCE.toDto(updatedWallet);
    }

    private Wallet findWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for user with id: " + userId));
    }
}