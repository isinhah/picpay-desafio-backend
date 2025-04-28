package com.desafio.picpay_simplificado.service;

import com.desafio.picpay_simplificado.entity.Transaction;
import com.desafio.picpay_simplificado.entity.User;
import com.desafio.picpay_simplificado.entity.enums.UserRole;
import com.desafio.picpay_simplificado.entity.Wallet;
import com.desafio.picpay_simplificado.repository.TransactionRepository;
import com.desafio.picpay_simplificado.repository.UserRepository;
import com.desafio.picpay_simplificado.web.dto.TransactionRequestDto;
import com.desafio.picpay_simplificado.web.dto.TransactionResponseDto;
import com.desafio.picpay_simplificado.web.exception.TransactionException;
import com.desafio.picpay_simplificado.web.mapper.TransactionMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Cacheable(value = "transactions", key = "#userId")
    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> findTransactionsByUser(Long userId, Pageable pageable) {
        log.info("Fetching transactions for user with ID: {}", userId);
        return transactionRepository.findAllByPayer_IdOrPayee_Id(userId, userId, pageable)
                .map(TransactionMapper.INSTANCE::toDto);
    }

    @CacheEvict(value = "transactions", key = "#requestDto.payerId()")
    @Transactional
    public TransactionResponseDto create(TransactionRequestDto requestDto) {
        log.info("Starting transaction creation: Payer ID: {}, Payee ID: {}, Amount: {}",
                requestDto.payerId(), requestDto.payeeId(), requestDto.amount());

        User payer = findUserById(requestDto.payerId());
        User payee = findUserById(requestDto.payeeId());

        validateTypeOfPayer(payer);

        validatePayerBalance(payer, requestDto.amount());

        log.info("Transferring amount {} from payer {} to payee {}",
                requestDto.amount(), payer.getId(), payee.getId());
        transferAmount(payer.getWallet(), payee.getWallet(), requestDto.amount());

        Transaction transaction = TransactionMapper.INSTANCE.toEntity(requestDto, payer, payee);

        Transaction savedTransaction = transactionRepository.save(transaction);

        log.info("Transaction successfully created. Transaction ID: {}", savedTransaction.getId());

        // notificationService.sendNotification(payee.getEmail());
        // log.info("Notification sent to payee: {}", payee.getEmail());

        return TransactionMapper.INSTANCE.toDto(savedTransaction);
    }

    public void validateTypeOfPayer(User payer) {
        log.debug("Validating payer type for user ID: {}", payer.getId());
        if (payer.getRole() == UserRole.MERCHANT) {
            log.warn("Merchant user (ID: {}) tried to send money, which is not allowed.", payer.getId());
            throw new TransactionException("Merchants cannot send money, only receive.");
        }
    }

    public void validatePayerBalance(User payer, BigDecimal amount) {
        Wallet payerWallet = payer.getWallet();

        if (payerWallet == null || payerWallet.getBalance().compareTo(amount) < 0) {
            log.error("Insufficient balance for user ID: {}. Attempted transaction amount: {}",
                    payer.getId(), amount);
            throw new TransactionException("Insufficient balance for the transaction.");
        }
        log.debug("Sufficient balance for user ID: {}. Proceeding with transaction.", payer.getId());
    }

    private void transferAmount(Wallet payerWallet, Wallet payeeWallet, BigDecimal amount) {
        log.debug("Transferring {} from payer's wallet to payee's wallet.", amount);
        payerWallet.setBalance(payerWallet.getBalance().subtract(amount));
        payeeWallet.setBalance(payeeWallet.getBalance().add(amount));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }
}