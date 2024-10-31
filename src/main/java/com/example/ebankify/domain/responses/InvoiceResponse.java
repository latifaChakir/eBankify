package com.example.ebankify.domain.responses;

import com.example.ebankify.domain.dtos.InvoiceDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponse {
    private InvoiceDTO invoiceDTO;
    private String message;
    private int statusCode;
}
