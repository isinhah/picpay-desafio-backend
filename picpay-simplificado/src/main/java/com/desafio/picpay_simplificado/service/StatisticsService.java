package com.desafio.picpay_simplificado.service;

import com.desafio.picpay_simplificado.entity.Transaction;
import com.desafio.picpay_simplificado.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class StatisticsService {

    private final TransactionRepository transactionRepository;

    @Cacheable(value = "transactionStatistics", key = "#root.methodName + #oneMinuteAgo")
    public DoubleSummaryStatistics getStatistics() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMinuteAgo = now.minusSeconds(60);

        List<Transaction> recentTransactions = transactionRepository.findByCreatedAtAfter(oneMinuteAgo);

        return recentTransactions.stream()
                .map(Transaction::getAmount)
                .filter(Objects::nonNull)
                .mapToDouble(BigDecimal::doubleValue)
                .summaryStatistics();
    }
}