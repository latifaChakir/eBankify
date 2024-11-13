package com.example.ebankify.domain.requests;
import com.example.ebankify.domain.enums.TransactionStatus;
import com.example.ebankify.domain.enums.TransactionType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequest {
    @NotNull(message = "Le type de transaction ne peut pas être nul")
    private TransactionType type;

    @Min(value = 0, message = "Le montant doit être positif")
    private double amount;

    @NotNull(message = "Le statut de la transaction ne peut pas être nul")
    private TransactionStatus status;

    @NotNull(message = "L'identifiant du compte source ne peut pas être nul")
    private Long sourceAccountId;

    @NotNull(message = "L'identifiant du compte de destination ne peut pas être nul")
    private Long destinationAccountId;

    @Future(message = "La date de prochaine exécution doit être dans le futur")
    private LocalDate nextExecutionDate;

    @AssertTrue(message = "La date de prochaine exécution doit être fournie pour une transaction planifiée")
    public boolean isNextExecutionDateValid() {
        return type != TransactionType.SCHEDULED || nextExecutionDate != null;
    }

}
