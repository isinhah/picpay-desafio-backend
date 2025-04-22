package com.desafio.picpay_simplificado.service;

import com.desafio.picpay_simplificado.entity.User;
import com.desafio.picpay_simplificado.entity.Wallet;
import com.desafio.picpay_simplificado.repository.WalletRepository;
import com.desafio.picpay_simplificado.web.dto.WalletDepositDto;
import com.desafio.picpay_simplificado.web.dto.WalletResponseDto;
import com.desafio.picpay_simplificado.web.exception.WalletNotFoundException;
import com.desafio.picpay_simplificado.web.mapper.WalletMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalletService {

    private final WalletRepository walletRepository;

    @Transactional(readOnly = true)
    public WalletResponseDto findByUser(Long userId) {
        log.info("Searching for wallet by user ID: {}", userId);
        Wallet wallet = findWalletByUserId(userId);
        return WalletMapper.INSTANCE.toDto(wallet);
    }

    public void createWallet(User user) {
        log.info("Creating wallet for user ID: {}", user.getId());
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(BigDecimal.ZERO);
        walletRepository.save(wallet);
        log.info("Wallet successfully created for user ID: {}", user.getId());
    }

    @Transactional
    public WalletResponseDto depositToWallet(Long userId, WalletDepositDto depositDto) {
        log.info("Depositing amount {} into wallet of user ID: {}", depositDto.amount(), userId);
        Wallet wallet = findWalletByUserId(userId);
        wallet.setBalance(wallet.getBalance().add(depositDto.amount()));

        Wallet updatedWallet = walletRepository.save(wallet);
        log.info("Deposit successful. New balance for user ID {}: {}", userId, updatedWallet.getBalance());

        return WalletMapper.INSTANCE.toDto(updatedWallet);
    }

    private Wallet findWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for user with id: " + userId));
    }
}