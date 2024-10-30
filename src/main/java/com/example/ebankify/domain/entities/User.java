package com.example.ebankify.domain.entities;

import com.example.ebankify.domain.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Le nom ne peut pas être vide")
    private String name;
    @Min(value = 18, message = "L'âge doit être d'au moins 18 ans")
    private int age;
    @Email(message = "L'adresse email n'est pas valide")
    @Column(unique = true)
    private String email;
    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    private String password;
    @NotNull(message = "Le revenu mensuel ne peut pas être nul")
    private double monthlyIncome;
    @NotNull(message = "Le revenu mensuel ne peut pas être nul")
    private int creditScore;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le rôle ne peut pas être nul")
    private Role role;
}
