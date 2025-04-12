package com.desafio.picpay_simplificado.web.mapper;

import com.desafio.picpay_simplificado.entity.User;
import com.desafio.picpay_simplificado.entity.UserRole;
import com.desafio.picpay_simplificado.web.dto.UserRegisterDto;
import com.desafio.picpay_simplificado.web.dto.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "wallet", ignore = true)
    @Mapping(target = "sentTransactions", ignore = true)
    @Mapping(target = "receivedTransactions", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "document", target = "document")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role", qualifiedByName = "mapStringToRole")
    User toUser(UserRegisterDto registerDto);

    @Mapping(source = "role", target = "role", qualifiedByName = "mapStringToRole")
    UserResponseDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "wallet", ignore = true)
    @Mapping(target = "sentTransactions", ignore = true)
    @Mapping(target = "receivedTransactions", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "role", target = "role", qualifiedByName = "mapStringToRole")
    void updateFromDto(UserRegisterDto updateDto, @MappingTarget User user);

    @Named("mapStringToRole")
    default UserRole mapStringToRole(String role) {
        try {
            return UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new RuntimeException("Invalid role: " + role);
        }
    }
}