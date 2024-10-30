package com.example.ebankify.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "loans")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(value = 0, message = "Le montant principal ne peut pas être négatif")
    private double principal;

    @Min(value = 0, message = "Le taux d'intérêt ne peut pas être négatif")
    private double interestRate;
    @Min(value = 1, message = "La durée doit être d'au moins 1 mois")
    private int termMonths;
    private boolean approved;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}

