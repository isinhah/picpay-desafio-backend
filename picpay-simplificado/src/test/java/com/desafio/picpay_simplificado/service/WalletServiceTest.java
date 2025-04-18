package com.desafio.picpay_simplificado.service;


import com.desafio.picpay_simplificado.entity.Wallet;
import com.desafio.picpay_simplificado.repository.WalletRepository;
import com.desafio.picpay_simplificado.web.dto.WalletResponseDto;
import com.desafio.picpay_simplificado.web.exception.WalletNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.desafio.picpay_simplificado.constants.UserConstant.USER;
import static com.desafio.picpay_simplificado.constants.WalletConstants.CREATED_AT;
import static com.desafio.picpay_simplificado.constants.WalletConstants.UPDATED_AT;
import static com.desafio.picpay_simplificado.constants.WalletConstants.WALLET;
import static com.desafio.picpay_simplificado.constants.WalletConstants.WALLET_BALANCE;
import static com.desafio.picpay_simplificado.constants.WalletConstants.WALLET_DEPOSIT_DTO;
import static com.desafio.picpay_simplificado.constants.WalletConstants.WALLET_ID;
import static com.desafio.picpay_simplificado.constants.WalletConstants.WALLET_RESPONSE_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    private Wallet walletToDeposit;
    private Wallet updatedWallet;
    private BigDecimal expectedBalance;

    @BeforeEach
    void setUp() {
        walletToDeposit = new Wallet(
                WALLET_ID,
                WALLET_BALANCE,
                USER,
                CREATED_AT,
                UPDATED_AT
        );

        expectedBalance = WALLET_BALANCE.add(WALLET_DEPOSIT_DTO.amount());

        updatedWallet = new Wallet(
                WALLET_ID,
                expectedBalance,
                USER,
                CREATED_AT,
                LocalDateTime.now()
        );
    }


    @Test
    void findByUser_ShouldReturnWalletResponseDto_WhenSuccessful() {
        when(walletRepository.findByUserId(USER.getId())).thenReturn(Optional.of(WALLET));

        WalletResponseDto response = walletService.findByUser(USER.getId());

        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(WALLET_RESPONSE_DTO);

        verify(walletRepository).findByUserId(USER.getId());
    }

    @Test
    void findByUser_ShouldThrowWalletNotFoundException_WhenWalletNotFound() {
        when(walletRepository.findByUserId(USER.getId())).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> walletService.findByUser(USER.getId()));
    }

    @Test
    void createWallet_ShouldSaveWalletWithZeroBalance_WhenSuccessful() {
        walletService.createWallet(USER);

        verify(walletRepository).save(argThat(wallet ->
                wallet.getUser().equals(USER)
                        && wallet.getBalance().compareTo(BigDecimal.ZERO) == 0
        ));
    }

    @Test
    void depositToWallet_ShouldAddAmountToWalletAndReturnUpdatedDto_WhenSuccessful() {
        when(walletRepository.findByUserId(USER.getId())).thenReturn(Optional.of(walletToDeposit));
        when(walletRepository.save(walletToDeposit)).thenReturn(updatedWallet);

        WalletResponseDto response = walletService.depositToWallet(USER.getId(), WALLET_DEPOSIT_DTO);

        assertThat(response.balance()).isEqualTo(expectedBalance);
        verify(walletRepository).save(walletToDeposit);
    }

    @Test
    void depositToWallet_ShouldThrowWalletNotFoundException_WhenWalletNotFound() {
        when(walletRepository.findByUserId(USER.getId())).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class,
                () -> walletService.depositToWallet(USER.getId(), WALLET_DEPOSIT_DTO));
    }
}