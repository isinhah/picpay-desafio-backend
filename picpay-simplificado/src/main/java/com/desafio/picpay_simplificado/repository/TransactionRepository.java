package com.desafio.picpay_simplificado.repository;

import com.desafio.picpay_simplificado.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findAllByPayer_IdOrPayee_Id(Long payerId, Long payeeId, Pageable pageable);

}