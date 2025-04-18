package com.desafio.picpay_simplificado.service;

import com.desafio.picpay_simplificado.entity.Transaction;
import com.desafio.picpay_simplificado.entity.User;
import com.desafio.picpay_simplificado.entity.Wallet;
import com.desafio.picpay_simplificado.repository.TransactionRepository;
import com.desafio.picpay_simplificado.repository.UserRepository;
import com.desafio.picpay_simplificado.web.dto.TransactionRequestDto;
import com.desafio.picpay_simplificado.web.dto.TransactionResponseDto;
import com.desafio.picpay_simplificado.web.exception.TransactionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.desafio.picpay_simplificado.constants.TransactionConstants.TRANSACTION;
import static com.desafio.picpay_simplificado.constants.TransactionConstants.TRANSACTION_AMOUNT;
import static com.desafio.picpay_simplificado.constants.TransactionConstants.TRANSACTION_REQUEST_DTO;
import static com.desafio.picpay_simplificado.constants.TransactionConstants.TRANSACTION_RESPONSE_DTO;
import static com.desafio.picpay_simplificado.constants.UserConstant.MERCHANT;
import static com.desafio.picpay_simplificado.constants.UserConstant.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        Wallet payerWallet = new Wallet();
        payerWallet.setBalance(new BigDecimal("100.00"));
        USER.setWallet(payerWallet);

        Wallet payeeWallet = new Wallet();
        payeeWallet.setBalance(new BigDecimal("50.00"));
        MERCHANT.setWallet(payeeWallet);
    }

    @Test
    void create_ShouldReturnTransactionResponseDto_WhenSuccessful() {
        when(userRepository.findById(USER.getId())).thenReturn(Optional.of(USER));
        when(userRepository.findById(MERCHANT.getId())).thenReturn(Optional.of(MERCHANT));

        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(TRANSACTION);

        TransactionResponseDto response = transactionService.create(TRANSACTION_REQUEST_DTO);

        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(TRANSACTION_RESPONSE_DTO);

        verify(userRepository).findById(USER.getId());
        verify(userRepository).findById(MERCHANT.getId());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void create_ShouldThrowTransactionException_WhenPayerIsMerchant() {
        User merchantAsPayer = MERCHANT;
        when(userRepository.findById(merchantAsPayer.getId())).thenReturn(Optional.of(merchantAsPayer));
        when(userRepository.findById(USER.getId())).thenReturn(Optional.of(USER)); // payee

        TransactionRequestDto dto = new TransactionRequestDto(
                TRANSACTION_AMOUNT,
                merchantAsPayer.getId(),
                USER.getId()
        );

        assertThrows(TransactionException.class, () -> transactionService.create(dto));
        verify(userRepository).findById(merchantAsPayer.getId());
    }

    @Test
    void create_ShouldThrowTransactionException_WhenInsufficientBalance() {
        USER.getWallet().setBalance(new BigDecimal("10.00"));
        when(userRepository.findById(USER.getId())).thenReturn(Optional.of(USER));
        when(userRepository.findById(MERCHANT.getId())).thenReturn(Optional.of(MERCHANT));

        TransactionRequestDto dto = new TransactionRequestDto(
                TRANSACTION_AMOUNT,
                USER.getId(),
                MERCHANT.getId()
        );

        assertThrows(TransactionException.class, () -> transactionService.create(dto));
        verify(userRepository).findById(USER.getId());
        verify(userRepository).findById(MERCHANT.getId());
    }

    @Test
    void findTransactionsByUser_ShouldReturnPagedDtos_WhenSuccessful() {
        Page<Transaction> page = new PageImpl<>(List.of(TRANSACTION));
        when(transactionRepository.findAllByPayer_IdOrPayee_Id(
                USER.getId(), USER.getId(), Pageable.unpaged()))
                .thenReturn(page);

        Page<TransactionResponseDto> result = transactionService
                .findTransactionsByUser(USER.getId(), Pageable.unpaged());

        assertThat(result.getContent())
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(TRANSACTION_RESPONSE_DTO);
    }
}