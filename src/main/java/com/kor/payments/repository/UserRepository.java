package com.kor.payments.repository;

import com.kor.payments.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
//    User findById( username);
}
