package com.desafio.picpay_simplificado.service;

import com.desafio.picpay_simplificado.entity.User;
import com.desafio.picpay_simplificado.repository.UserRepository;
import com.desafio.picpay_simplificado.utils.UserValidator;
import com.desafio.picpay_simplificado.web.dto.UserResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static com.desafio.picpay_simplificado.constants.UserConstant.USER;
import static com.desafio.picpay_simplificado.constants.UserConstant.USER_DOCUMENT;
import static com.desafio.picpay_simplificado.constants.UserConstant.USER_EMAIL;
import static com.desafio.picpay_simplificado.constants.UserConstant.USER_ID;
import static com.desafio.picpay_simplificado.constants.UserConstant.USER_PASSWORD;
import static com.desafio.picpay_simplificado.constants.UserConstant.USER_REQUEST_DTO;
import static com.desafio.picpay_simplificado.constants.UserConstant.USER_RESPONSE_DTO;
import static com.desafio.picpay_simplificado.constants.UserConstant.USER_ROLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletService walletService;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserService userService;

    @Test
    void findById_ShouldReturnUserResponseDto_WhenSuccessful() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(USER));

        UserResponseDto response = userService.findById(USER_ID);

        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(USER_RESPONSE_DTO);

        verify(userRepository).findById(USER_ID);
    }

    @Test
    void findById_ShouldThrowEntityNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findById(USER_ID));
    }

    @Test
    void findAll_ShouldReturnPageOfUserResponseDto_WhenSuccessful() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(USER));

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<UserResponseDto> result = userService.findAll(pageable);

        assertThat(result.getContent())
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(USER_RESPONSE_DTO);

        verify(userRepository).findAll(pageable);
    }

    @Test
    void create_ShouldSaveAndReturnUserResponseDto_WhenSuccessful() {
        String encodedPassword = "encoded_password";

        when(userValidator.encodePassword(USER_REQUEST_DTO.password())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(USER);

        UserResponseDto response = userService.create(USER_REQUEST_DTO);

        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(USER_RESPONSE_DTO);

        verify(userValidator).validateUserEmailAndDocument(USER_EMAIL, USER_DOCUMENT);
        verify(userValidator).validateUserRoleAndDocument(USER_ROLE, USER_DOCUMENT);
        verify(userValidator).encodePassword(USER_PASSWORD);
        verify(userRepository).save(any(User.class));
        verify(walletService).createWallet(USER);
    }

    @Test
    void update_ShouldReturnUserResponseDto_WhenSuccessful() {
        String encodedPassword = "encoded_password";

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(USER));
        when(userValidator.encodePassword(USER_REQUEST_DTO.password())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(USER);

        UserResponseDto response = userService.update(USER_ID, USER_REQUEST_DTO);

        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(USER_RESPONSE_DTO);

        verify(userValidator).validateUserUpdateEmailAndDocument(USER_ID, USER_EMAIL, USER_DOCUMENT);
        verify(userValidator).validateUserRoleAndDocument(USER_ROLE, USER_DOCUMENT);
        verify(userValidator).encodePassword(USER_PASSWORD);
        verify(userRepository).save(USER);
    }

    @Test
    void update_ShouldThrowEntityNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.update(USER_ID, USER_REQUEST_DTO));
    }

    @Test
    void delete_ShouldRemoveUser_WhenSuccessful() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(USER));

        userService.delete(USER_ID);

        verify(userRepository).delete(USER);
    }

    @Test
    void delete_ShouldThrowEntityNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.delete(USER_ID));
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenSuccessful() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(USER));

        User found = userService.findByEmail(USER_EMAIL);

        assertNotNull(found);
        assertEquals(USER.getEmail(), found.getEmail());
    }

    @Test
    void findByEmail_ShouldThrowEntityNotFoundException_WhenNotFound() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findByEmail(USER_EMAIL));
    }
}