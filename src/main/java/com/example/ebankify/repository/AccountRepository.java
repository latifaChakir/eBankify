package com.example.ebankify.repository;

import com.example.ebankify.domain.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
