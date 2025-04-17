package com.desafio.picpay_simplificado.web.controller;

import com.desafio.picpay_simplificado.service.UserService;
import com.desafio.picpay_simplificado.web.dto.UserRequestDto;
import com.desafio.picpay_simplificado.web.dto.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PreAuthorize("#id == authentication.principal or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto responseDto = userService.findById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(Pageable pageable) {
        Page<UserResponseDto> page = userService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @PreAuthorize("#id == authentication.principal or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> alterUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto registerDto) {
        UserResponseDto responseDto = userService.update(id, registerDto);
        return ResponseEntity.ok(responseDto);
    }

    @PreAuthorize("#id == authentication.principal")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}