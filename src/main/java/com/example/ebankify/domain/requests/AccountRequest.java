package com.example.ebankify.domain.requests;
import com.example.ebankify.domain.entities.Bank;
import com.example.ebankify.domain.enums.AccountStatus;
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
public class AccountRequest {
    @NotNull(message = "Le solde ne peut pas être nul")
    @Min(value = 0, message = "Le solde doit être positif")
    private double balance;

    @NotNull(message = "Le numéro de compte ne peut pas être nul")
    private String accountNumber;

    @NotNull(message = "L'état du compte ne peut pas être nul")
    private AccountStatus status;

    @NotNull(message = "L'identifiant de l'utilisateur ne peut pas être nul")
    private Long userId;

    @NotNull(message = "Le banque ne peut pas être nulle")
    private Long bankId;
}
