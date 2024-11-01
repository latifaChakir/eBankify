package com.example.ebankify.controller;

import com.example.ebankify.domain.dtos.InvoiceDTO;
import com.example.ebankify.domain.requests.InvoiceRequest;
import com.example.ebankify.domain.responses.InvoiceResponse;
import com.example.ebankify.service.InvoiceService;
import jakarta.persistence.EntityTransaction;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "http://localhost:8080")
@AllArgsConstructor
public class InvoiceController {
    private InvoiceService invoiceService;
    @PostMapping("/save")
    public ResponseEntity<InvoiceResponse> saveInvoice(@Valid @RequestBody InvoiceRequest invoiceRequest) {
        InvoiceDTO invoiceDTO = invoiceService.saveInvoice(invoiceRequest);
        InvoiceResponse invoiceResponse = InvoiceResponse.builder()
                .invoiceDTO(invoiceDTO)
                .message("Invoice saved successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(invoiceResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable Long id) {
        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(id);
        InvoiceResponse invoiceResponse = InvoiceResponse.builder()
                .invoiceDTO(invoiceDTO)
                .message("Invoice retrieved successfully")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(invoiceResponse);
    }
//    @PutMapping("/update/{id}")
//    public ResponseEntity<InvoiceResponse> updateInvoice(@PathVariable Long id, @Valid @RequestBody InvoiceRequest invoiceRequest) {
//        InvoiceDTO invoiceDTO = invoiceService.updateInvoice(id, invoiceRequest);
//        InvoiceResponse invoiceResponse = InvoiceResponse.builder()
//                .invoiceDTO(invoiceDTO)
//                .message("Invoice updated successfully")
//                .statusCode(HttpStatus.OK.value())
//                .build();
//        return ResponseEntity.ok(invoiceResponse);
//    }
    @DeleteMapping("/{id}")
    public ResponseEntity<InvoiceResponse> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoiceById(id);
        InvoiceResponse invoiceResponse = InvoiceResponse.builder()
                .message("Invoice deleted successfully")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(invoiceResponse);
    }
    @GetMapping("/all")
    public ResponseEntity<InvoiceResponse> getAllInvoices() {
        List<InvoiceDTO> invoiceDTOs = invoiceService.getInvoices();
        InvoiceResponse invoiceResponse = InvoiceResponse.builder()
                .invoices(invoiceDTOs)
               .message("All invoices retrieved successfully")
               .statusCode(HttpStatus.OK.value())
               .build();
        return ResponseEntity.ok(invoiceResponse);
    }

}

