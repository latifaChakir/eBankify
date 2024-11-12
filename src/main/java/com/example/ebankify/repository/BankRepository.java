package com.example.ebankify.repository;

import com.example.ebankify.domain.entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long>{
}
