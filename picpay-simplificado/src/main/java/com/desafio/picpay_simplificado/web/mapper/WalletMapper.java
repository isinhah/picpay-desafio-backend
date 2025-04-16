package com.desafio.picpay_simplificado.web.mapper;

import com.desafio.picpay_simplificado.entity.Wallet;
import com.desafio.picpay_simplificado.web.dto.WalletResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WalletMapper {

    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

    WalletResponseDto toDto(Wallet wallet);
}