package com.desafio.picpay_simplificado.repository;

import com.desafio.picpay_simplificado.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findAllByPayee_Id(UUID payeeId, Pageable pageable);
}