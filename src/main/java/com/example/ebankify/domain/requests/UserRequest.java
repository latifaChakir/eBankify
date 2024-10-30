package com.example.ebankify.domain.requests;
import com.example.ebankify.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "Le nom ne peut pas être vide")
    private String name;

    @Min(value = 18, message = "L'âge doit être d'au moins 18 ans")
    private int age;

    @Email(message = "L'adresse email n'est pas valide")
    private String email;

    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    private String password;

    @NotNull(message = "Le revenu mensuel ne peut pas être nul")
    private double monthlyIncome;

    @NotNull(message = "Le score de crédit ne peut pas être nul")
    private int creditScore;

    @NotNull(message = "Le rôle ne peut pas être nul")
    private Role role;
}
