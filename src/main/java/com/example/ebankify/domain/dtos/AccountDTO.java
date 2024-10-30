package com.example.ebankify.domain.dtos;

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
public class AccountDTO {
    private Long id;

    @Min(value = 0, message = "Le solde ne peut pas être négatif")
    private double balance;

    @NotNull(message = "Le numéro de compte ne peut pas être nul")
    private String accountNumber;

    @NotNull(message = "L'état du compte ne peut pas être nul")
    private AccountStatus status;

    private Long userId;
}