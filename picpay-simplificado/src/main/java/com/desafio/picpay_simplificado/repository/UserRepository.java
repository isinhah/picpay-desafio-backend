package com.desafio.picpay_simplificado.repository;

import com.desafio.picpay_simplificado.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByDocument(String document);

    Optional<User> findByEmail(String email);
    Optional<User> findByDocument(String document);
}