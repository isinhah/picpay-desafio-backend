package com.desafio.picpay_simplificado.web.mapper;

import com.desafio.picpay_simplificado.entity.Transaction;
import com.desafio.picpay_simplificado.entity.User;
import com.desafio.picpay_simplificado.web.dto.TransactionRequestDto;
import com.desafio.picpay_simplificado.web.dto.TransactionResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(source = "payer.id", target = "payer")
    @Mapping(source = "payee.id", target = "payee")
    TransactionResponseDto toDto(Transaction transaction);

    default Transaction toEntity(TransactionRequestDto dto, User payer, User payee) {
        Transaction transaction = new Transaction();
        transaction.setAmount(dto.amount());
        transaction.setPayer(payer);
        transaction.setPayee(payee);
        return transaction;
    }
}