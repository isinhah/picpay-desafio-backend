package com.desafio.picpay_simplificado.web.mapper;

import com.desafio.picpay_simplificado.entity.User;
import com.desafio.picpay_simplificado.entity.Wallet;
import com.desafio.picpay_simplificado.web.dto.WalletRequestDto;
import com.desafio.picpay_simplificado.web.dto.WalletResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

@Mapper
public interface WalletMapper {

    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "userId", target = "user.id")
    Wallet toWallet(WalletRequestDto requestDto);

    @Mapping(source = "user.id", target = "userId")
    WalletResponseDto toDto(Wallet wallet);

    default Wallet createWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(BigDecimal.ZERO);
        return wallet;
    }
}