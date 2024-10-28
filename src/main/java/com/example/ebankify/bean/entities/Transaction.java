package com.example.ebankify.bean.entities;

import com.example.ebankify.bean.enums.TransactionStatus;
import com.example.ebankify.bean.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Le type de transaction ne peut pas être nul")
    private TransactionType type;
    @Min(value = 0, message = "Le montant doit être positif")
    private double amount;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le statut de la transaction ne peut pas être nul")
    private TransactionStatus status;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
