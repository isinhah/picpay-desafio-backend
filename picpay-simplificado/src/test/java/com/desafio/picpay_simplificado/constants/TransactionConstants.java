package com.desafio.picpay_simplificado.constants;

import com.desafio.picpay_simplificado.entity.Transaction;
import com.desafio.picpay_simplificado.web.dto.TransactionRequestDto;
import com.desafio.picpay_simplificado.web.dto.TransactionResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.desafio.picpay_simplificado.constants.UserConstant.USER;
import static com.desafio.picpay_simplificado.constants.UserConstant.MERCHANT;

public class TransactionConstants {

    public static final Long TRANSACTION_ID = 1L;
    public static final BigDecimal TRANSACTION_AMOUNT = new BigDecimal("25.00");

    public static final Transaction TRANSACTION = new Transaction(
            TRANSACTION_ID,
            TRANSACTION_AMOUNT,
            UserConstant.USER,
            UserConstant.MERCHANT,
            LocalDateTime.now()
    );

    public static final TransactionRequestDto TRANSACTION_REQUEST_DTO = new TransactionRequestDto(
            TRANSACTION_AMOUNT,
            USER.getId(),
            MERCHANT.getId()
    );

    public static final TransactionResponseDto TRANSACTION_RESPONSE_DTO = new TransactionResponseDto(
            TRANSACTION_AMOUNT,
            USER.getId(),
            MERCHANT.getId()
    );
}