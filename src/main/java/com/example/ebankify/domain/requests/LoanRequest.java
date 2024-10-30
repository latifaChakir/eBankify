package com.example.ebankify.domain.requests;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanRequest {
    @NotNull(message = "Le montant principal ne peut pas être nul")
    @Min(value = 0, message = "Le montant principal ne peut pas être négatif")
    private double principal;

    @NotNull(message = "Le taux d'intérêt ne peut pas être nul")
    @Min(value = 0, message = "Le taux d'intérêt ne peut pas être négatif")
    private double interestRate;

    @NotNull(message = "La durée doit être spécifiée")
    @Min(value = 1, message = "La durée doit être d'au moins 1 mois")
    private int termMonths;

    private boolean approved;

    @NotNull(message = "L'identifiant de l'utilisateur ne peut pas être nul")
    private Long userId;
}
