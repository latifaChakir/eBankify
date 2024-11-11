package com.example.ebankify.domain.vm;

import com.example.ebankify.domain.dtos.InvoiceDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceVM {
    private InvoiceDTO invoiceDTO;
    private String message;
    private int statusCode;
    private List<InvoiceDTO> invoices;
}
