package com.example.ebankify.repository;

import com.example.ebankify.domain.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a JOIN FETCH a.user")
    List<Account> findAllWithUser();
}
