package com.kor.payments.repository;

import com.kor.payments.domain.Account;
import com.kor.payments.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findById(long id);

    List<Account> findAll();

    List<Account> findAccountsByActiveTrueAndUser(User user);

    Page<Account> findAll(Pageable pageable);

    List<Account> findAccountsByUser(User user);

}
