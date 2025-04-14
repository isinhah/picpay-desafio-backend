package com.desafio.picpay_simplificado.web.controller;

import com.desafio.picpay_simplificado.service.TransactionService;
import com.desafio.picpay_simplificado.web.dto.TransactionRequestDto;
import com.desafio.picpay_simplificado.web.dto.TransactionResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDto> createTransaction(@Valid @RequestBody TransactionRequestDto requestDto) {
        TransactionResponseDto response = transactionService.create(requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<TransactionResponseDto>> getUserTransactions(
            @PathVariable UUID userId,
            Pageable pageable
    ) {
        Page<TransactionResponseDto> transactions = transactionService.findTransactionsByUser(userId, pageable);
        return ResponseEntity.ok(transactions);
    }
}