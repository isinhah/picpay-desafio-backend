package com.desafio.picpay_simplificado.web.mapper;

import com.desafio.picpay_simplificado.entity.Transaction;
import com.desafio.picpay_simplificado.entity.User;
import com.desafio.picpay_simplificado.web.dto.TransactionRequestDto;
import com.desafio.picpay_simplificado.web.dto.TransactionResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "payerId", target = "payer.id")
    @Mapping(source = "payeeId", target = "payee.id")
    Transaction toTransaction(TransactionRequestDto requestDto);

    @Mapping(source = "payer.id", target = "payer")
    @Mapping(source = "payee.id", target = "payee")
    TransactionResponseDto toDto(Transaction transaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "payerId", target = "payer")
    @Mapping(source = "payeeId", target = "payee")
    void updateFromDto(TransactionRequestDto updateDto, @MappingTarget Transaction transaction);

    default User map(Long id) {
        if (id == null) return null;
        User user = new User();
        user.setId(id);
        return user;
    }

    default Transaction toEntity(TransactionRequestDto dto, User payer, User payee) {
        Transaction transaction = new Transaction();
        transaction.setAmount(dto.amount());
        transaction.setPayer(payer);
        transaction.setPayee(payee);
        return transaction;
    }
}