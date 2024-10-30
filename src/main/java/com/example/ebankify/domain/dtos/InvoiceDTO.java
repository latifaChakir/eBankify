package com.example.ebankify.domain.dtos;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceDTO {
    private Long id;
    @Min(value = 0, message = "Le montant dû ne peut pas être négatif")
    private double amountDue;
    private Date dueDate;
    private Long userId;
}