package com.example.ebankify.domain.entities;

import com.example.ebankify.domain.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "accounts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double balance;
    @Column(unique = true)
    private String accountNumber;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "sourceAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> sourceTransactions;

    @OneToMany(mappedBy = "destinationAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> destinationTransactions;
    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;
}
