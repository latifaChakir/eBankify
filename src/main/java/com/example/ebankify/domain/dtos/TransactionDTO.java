package com.example.ebankify.domain.dtos;

import com.example.ebankify.domain.enums.TransactionStatus;
import com.example.ebankify.domain.enums.TransactionType;
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
public class TransactionDTO {
    private Long id;
    @NotNull(message = "Le type de transaction ne peut pas être nul")
    private TransactionType type;
    @Min(value = 0, message = "Le montant doit être positif")
    private double amount;
    @NotNull(message = "Le statut de la transaction ne peut pas être nul")
    private TransactionStatus status;
    private Long sourceAccountId;
    private Long destinationAccountId;
}
