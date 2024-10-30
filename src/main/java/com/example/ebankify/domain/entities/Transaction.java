package com.example.ebankify.domain.entities;

import com.example.ebankify.domain.enums.TransactionStatus;
import com.example.ebankify.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private double amount;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    @ManyToOne
    @JoinColumn(name = "source_account_id")
    private Account sourceAccount;
    @ManyToOne
    @JoinColumn(name = "destination_account_id")
    private Account destinationAccount;
}
