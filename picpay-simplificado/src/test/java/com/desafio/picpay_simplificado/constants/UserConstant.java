package com.desafio.picpay_simplificado.constants;

import com.desafio.picpay_simplificado.entity.User;
import com.desafio.picpay_simplificado.entity.enums.UserRole;
import com.desafio.picpay_simplificado.web.dto.UserRequestDto;
import com.desafio.picpay_simplificado.web.dto.UserResponseDto;

import java.time.LocalDateTime;

public class UserConstant {

    public static final Long USER_ID = 1L;
    public static final String USER_NAME = "John Doe";
    public static final String USER_EMAIL = "johndoe@example.com";
    public static final String USER_DOCUMENT = "12345678901";
    public static final String USER_PASSWORD = "12345678";
    public static final String USER_ROLE = "USER";
    public static final LocalDateTime CREATED_AT = LocalDateTime.now();
    public static final LocalDateTime UPDATED_AT = LocalDateTime.now();

    public static final User USER = new User(
            USER_ID,
            USER_NAME,
            USER_DOCUMENT,
            USER_EMAIL,
            USER_PASSWORD,
            UserRole.USER,
            CREATED_AT,
            UPDATED_AT,
            null,
            null,
            null
    );

    public static final User MERCHANT = new User(
            2L,
            "Loja",
            "00000000000000",
            "loja@email.com",
            "12345678",
            UserRole.MERCHANT,
            CREATED_AT,
            UPDATED_AT,
            null,
            null,
            null
    );

    public static final UserRequestDto USER_REQUEST_DTO = new UserRequestDto(
            USER_NAME,
            USER_DOCUMENT,
            USER_EMAIL,
            USER_PASSWORD,
            USER_ROLE
    );

    public static final UserResponseDto USER_RESPONSE_DTO = new UserResponseDto(
            USER_ID,
            USER_NAME,
            USER_EMAIL,
            USER_ROLE
    );
}