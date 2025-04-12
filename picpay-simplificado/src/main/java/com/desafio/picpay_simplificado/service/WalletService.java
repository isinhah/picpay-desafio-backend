package com.desafio.picpay_simplificado.service;

import com.desafio.picpay_simplificado.entity.User;
import com.desafio.picpay_simplificado.entity.Wallet;
import com.desafio.picpay_simplificado.repository.UserRepository;
import com.desafio.picpay_simplificado.repository.WalletRepository;
import com.desafio.picpay_simplificado.web.dto.WalletRequestDto;
import com.desafio.picpay_simplificado.web.dto.WalletResponseDto;
import com.desafio.picpay_simplificado.web.dto.WalletUpdateDto;
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
    public WalletResponseDto findWalletByUser(UUID userId) {
        Wallet wallet = findWalletByUserId(userId);
        return WalletMapper.INSTANCE.toDto(wallet);
    }

    @Transactional
    public WalletResponseDto createWallet(WalletRequestDto requestDto) {
        User user = findUserById(requestDto.userId());

        Wallet wallet = WalletMapper.INSTANCE.toEntity(requestDto, user);

        Wallet savedWallet = walletRepository.save(wallet);
        return WalletMapper.INSTANCE.toDto(savedWallet);
    }

    @Transactional
    public WalletResponseDto updateWallet(UUID userId, WalletUpdateDto updateDto) {
        findUserById(userId);
        Wallet wallet = findWalletByUserId(userId);

        WalletMapper.INSTANCE.updateFromDto(updateDto, wallet);

        Wallet updatedWallet = walletRepository.save(wallet);
        return WalletMapper.INSTANCE.toDto(updatedWallet);
    }

    private Wallet findWalletByUserId(UUID userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found for user with id: " + userId));
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }
}