package com.desafio.picpay_simplificado.service;

import com.desafio.picpay_simplificado.entity.Transaction;
import com.desafio.picpay_simplificado.entity.User;
import com.desafio.picpay_simplificado.entity.enums.UserRole;
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
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> findTransactionsByUser(UUID userId, Pageable pageable) {
        return transactionRepository.findAllByPayee_Id(userId, pageable)
                .map(TransactionMapper.INSTANCE::toDto);
    }

    @Transactional
    public TransactionResponseDto create(TransactionRequestDto requestDto) {
        User payer = findUserById(requestDto.payerId());
        User payee = findUserById(requestDto.payeeId());

        validateTypeOfPayer(payer);

//        if (!notificationService.isAuthorized()) {
//            throw new RuntimeException("Unauthorized transaction.");
//        }

        validatePayerBalance(payer, requestDto.amount());

        transferAmount(payer.getWallet(), payee.getWallet(), requestDto.amount());

        Transaction transaction = TransactionMapper.INSTANCE.toEntity(requestDto, payer, payee);

        Transaction savedTransaction = transactionRepository.save(transaction);

//        notificationService.sendNotification(payee.getEmail());

        return TransactionMapper.INSTANCE.toDto(savedTransaction);
    }

    public void validateTypeOfPayer(User payer) {
        if (payer.getRole() == UserRole.MERCHANT) {
            throw new IllegalArgumentException("Merchants cannot send money, only receive.");
        }
    }

    public void validatePayerBalance(User payer, BigDecimal amount) {
        Wallet payerWallet = payer.getWallet();

        if (payerWallet == null || payerWallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance for the transaction.");
        }
    }

    private void transferAmount(Wallet payerWallet, Wallet payeeWallet, BigDecimal amount) {
        payerWallet.setBalance(payerWallet.getBalance().subtract(amount));
        payeeWallet.setBalance(payeeWallet.getBalance().add(amount));
    }

    private User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }
}