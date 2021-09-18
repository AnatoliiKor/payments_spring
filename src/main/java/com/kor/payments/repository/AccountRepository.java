package com.kor.payments.repository;

import com.kor.payments.domain.Account;
import com.kor.payments.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findById(long id);

    Account findAccountByAccountName(String name);

    List<Account> findAll ();

    List<Account> findAccountsByUser (User user);

}
