package com.desafio.picpay_simplificado.constants;

import com.desafio.picpay_simplificado.entity.Wallet;
import com.desafio.picpay_simplificado.web.dto.WalletDepositDto;
import com.desafio.picpay_simplificado.web.dto.WalletResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.desafio.picpay_simplificado.constants.UserConstant.USER;

public class WalletConstants {

    public static final UUID WALLET_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    public static final BigDecimal WALLET_BALANCE = new BigDecimal("100.00");
    public static final LocalDateTime CREATED_AT = LocalDateTime.now();
    public static final LocalDateTime UPDATED_AT = LocalDateTime.now();

    public static final Wallet WALLET = new Wallet(
            WALLET_ID,
            WALLET_BALANCE,
            USER,
            CREATED_AT,
            UPDATED_AT
    );

    public static final WalletResponseDto WALLET_RESPONSE_DTO = new WalletResponseDto(
            WALLET_ID,
            WALLET_BALANCE
    );

    public static final WalletDepositDto WALLET_DEPOSIT_DTO = new WalletDepositDto(
            new BigDecimal("50.00")
    );
}
