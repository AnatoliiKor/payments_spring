package com.kor.payments.repository;

import com.kor.payments.domain.Transaction;
import com.kor.payments.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findAll(Pageable pageable);

    Page<Transaction> findByReceiver_User(User user, Pageable pageable);

    Page<Transaction> findByPayer_User(User user, Pageable pageable);

}
