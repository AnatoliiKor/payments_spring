package com.kor.payments.repository;

import com.kor.payments.domain.AccountRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRequestRepository extends JpaRepository<AccountRequest, Long> {

//    List<AccountRequest> findAll();
}
