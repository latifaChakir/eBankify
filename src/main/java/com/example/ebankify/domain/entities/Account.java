package com.example.ebankify.domain.entities;

import com.example.ebankify.domain.enums.AccountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Min(value = 0, message = "Le solde ne peut pas être négatif")
    private double balance;

    @NotNull(message = "Le numéro de compte ne peut pas être nul")
    @Column(unique = true)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "L'état du compte ne peut pas être nul")
    private AccountStatus status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
