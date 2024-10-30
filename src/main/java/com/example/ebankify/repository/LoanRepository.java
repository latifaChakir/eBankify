package com.example.ebankify.repository;

import com.example.ebankify.domain.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
