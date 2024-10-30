package com.example.ebankify.domain.requests;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceRequest {
    @NotNull(message = "Le montant dû ne peut pas être nul")
    @Min(value = 0, message = "Le montant dû doit être positif")
    private double amountDue;

    @NotNull(message = "La date d'échéance ne peut pas être nulle")
    private Date dueDate;

    @NotNull(message = "L'identifiant de l'utilisateur ne peut pas être nul")
    private Long userId;
}
