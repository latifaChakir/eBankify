package com.example.ebankify.controller;

import com.example.ebankify.domain.dtos.TransactionDTO;
import com.example.ebankify.domain.requests.TransactionRequest;
import com.example.ebankify.domain.responses.TransactionResponse;
import com.example.ebankify.service.TransactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:8080")
@AllArgsConstructor
public class TransactionController {
    private TransactionService transactionService;
    @PostMapping("/save")
    public ResponseEntity<TransactionResponse> saveTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        TransactionDTO transactionDTO=transactionService.saveTransaction(transactionRequest);
        TransactionResponse response = TransactionResponse.builder()
                .transactionDTO(transactionDTO)
                .message("transaction saved successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/all")
    public ResponseEntity<TransactionResponse> getAllTransactions() {
        List<TransactionDTO> transactionDTOs = transactionService.getAllTransactions();
        TransactionResponse response = TransactionResponse.builder()
                .transactions(transactionDTOs)
                .message("all transactions found successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
        TransactionDTO transactionDTO = transactionService.getTransactionById(id);
        TransactionResponse response = TransactionResponse.builder()
                .transactionDTO(transactionDTO)
                .message("transaction found successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable Long id, @Valid @RequestBody TransactionRequest transactionRequest) {
        TransactionDTO transactionDTO = transactionService.updateTransaction(id, transactionRequest);
        TransactionResponse response = TransactionResponse.builder()
                .transactionDTO(transactionDTO)
                .message("transaction updated successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<TransactionResponse> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        TransactionResponse response = TransactionResponse.builder()
                .message("transaction deleted successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/approve/{id}")
    public ResponseEntity<TransactionResponse> approveTransaction(@PathVariable Long id) {
        transactionService.acceptTransaction(id);
        TransactionResponse response = TransactionResponse.builder()
                .message("transaction approved successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/reject/{id}")
    public ResponseEntity<TransactionResponse> rejectTransaction(@PathVariable Long id) {
        transactionService.cancelTransaction(id);
        TransactionResponse response = TransactionResponse.builder()
                .message("transaction rejected successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
}
