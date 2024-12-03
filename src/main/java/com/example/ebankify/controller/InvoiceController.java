package com.example.ebankify.controller;

import com.example.ebankify.domain.dtos.InvoiceDTO;
import com.example.ebankify.domain.requests.InvoiceRequest;
import com.example.ebankify.domain.vm.InvoiceVM;
import com.example.ebankify.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class InvoiceController {
    private InvoiceService invoiceService;
    @PostMapping("/save")
    public ResponseEntity<InvoiceVM> saveInvoice(@Valid @RequestBody InvoiceRequest invoiceRequest) {
        InvoiceDTO invoiceDTO = invoiceService.saveInvoice(invoiceRequest);
        InvoiceVM invoiceVM = InvoiceVM.builder()
                .invoiceDTO(invoiceDTO)
                .message("Invoice saved successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(invoiceVM);
    }
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceVM> getInvoiceById(@PathVariable Long id) {
        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(id);
        InvoiceVM invoiceVM = InvoiceVM.builder()
                .invoiceDTO(invoiceDTO)
                .message("Invoice retrieved successfully")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(invoiceVM);
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
    public ResponseEntity<InvoiceVM> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoiceById(id);
        InvoiceVM invoiceVM = InvoiceVM.builder()
                .message("Invoice deleted successfully")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(invoiceVM);
    }
    @GetMapping("/all")
    public ResponseEntity<InvoiceVM> getAllInvoices() {
        List<InvoiceDTO> invoiceDTOs = invoiceService.getInvoices();
        InvoiceVM invoiceVM = InvoiceVM.builder()
                .invoices(invoiceDTOs)
               .message("All invoices retrieved successfully")
               .statusCode(HttpStatus.OK.value())
               .build();
        return ResponseEntity.ok(invoiceVM);
    }

}

