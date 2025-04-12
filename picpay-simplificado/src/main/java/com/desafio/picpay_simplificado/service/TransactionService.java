package com.desafio.picpay_simplificado.service;

import com.desafio.picpay_simplificado.entity.Transaction;
import com.desafio.picpay_simplificado.entity.User;
import com.desafio.picpay_simplificado.entity.UserRole;
import com.desafio.picpay_simplificado.entity.Wallet;
import com.desafio.picpay_simplificado.repository.TransactionRepository;
import com.desafio.picpay_simplificado.repository.UserRepository;
import com.desafio.picpay_simplificado.web.dto.TransactionRequestDto;
import com.desafio.picpay_simplificado.web.dto.TransactionResponseDto;
import com.desafio.picpay_simplificado.web.mapper.TransactionMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> findTransactionsByUser(UUID userId, Pageable pageable) {
        return transactionRepository.findAllByPayee_Id(userId, pageable)
                .map(TransactionMapper.INSTANCE::toDto);
    }

    @Transactional
    public TransactionResponseDto createTransaction(TransactionRequestDto requestDto) {
        User payer = findUserById(requestDto.payerId());
        User payee = findUserById(requestDto.payeeId());

        validatePayerBalance(payer, requestDto.amount());
        validateTypeOfPayer(payer);

        Transaction transaction = TransactionMapper.INSTANCE.toEntity(requestDto, payer, payee);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return TransactionMapper.INSTANCE.toDto(savedTransaction);
    }

    public void validatePayerBalance(User payer, BigDecimal amount) {
        Wallet payerWallet = payer.getWallet();

        if (payerWallet == null || payerWallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance for the transaction.");
        }
    }

    public void validateTypeOfPayer(User payer) {
        if (payer.getRole() == UserRole.MERCHANT) {
            throw new IllegalArgumentException("Merchants cannot send money, only receive.");
        }
    }

    private User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }
}