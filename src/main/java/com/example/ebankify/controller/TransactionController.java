package com.example.ebankify.controller;

import com.example.ebankify.domain.dtos.TransactionDTO;
import com.example.ebankify.domain.elasticsearch.TransactionDocument;
import com.example.ebankify.domain.entities.Transaction;
import com.example.ebankify.domain.enums.TransactionStatus;
import com.example.ebankify.domain.enums.TransactionType;
import com.example.ebankify.domain.requests.TransactionRequest;
import com.example.ebankify.domain.vm.TransactionVM;
import com.example.ebankify.service.TransactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:8080")
@AllArgsConstructor
public class TransactionController {
    private TransactionService transactionService;
    @PostMapping("/save")
    public ResponseEntity<TransactionVM> saveTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        TransactionDTO transactionDTO=transactionService.saveTransaction(transactionRequest);
        TransactionVM response = TransactionVM.builder()
                .transactionDTO(transactionDTO)
                .message("transaction saved successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/all")
    public ResponseEntity<TransactionVM> getAllTransactions() {
        List<TransactionDTO> transactionDTOs = transactionService.getAllTransactions();
        TransactionVM response = TransactionVM.builder()
                .transactions(transactionDTOs)
                .message("all transactions found successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TransactionVM> getTransactionById(@PathVariable Long id) {
        TransactionDTO transactionDTO = transactionService.getTransactionById(id);
        TransactionVM response = TransactionVM.builder()
                .transactionDTO(transactionDTO)
                .message("transaction found successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<TransactionVM> updateTransaction(@PathVariable Long id, @Valid @RequestBody TransactionRequest transactionRequest) {
        TransactionDTO transactionDTO = transactionService.updateTransaction(id, transactionRequest);
        TransactionVM response = TransactionVM.builder()
                .transactionDTO(transactionDTO)
                .message("transaction updated successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<TransactionVM> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        TransactionVM response = TransactionVM.builder()
                .message("transaction deleted successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/approve/{id}")
    public ResponseEntity<TransactionVM> approveTransaction(@PathVariable Long id) {
        transactionService.acceptTransaction(id);
        TransactionVM response = TransactionVM.builder()
                .message("transaction approved successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/reject/{id}")
    public ResponseEntity<TransactionVM> rejectTransaction(@PathVariable Long id) {
        transactionService.cancelTransaction(id);
        TransactionVM response = TransactionVM.builder()
                .message("transaction rejected successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/history/{accountId}")
    public ResponseEntity<TransactionVM> getTransactionHistory(@PathVariable Long accountId) {
        List<TransactionDTO> transactionDTOs = transactionService.getTransactionHistoryByAccountId(accountId);
        TransactionVM response = TransactionVM.builder()
                .transactions(transactionDTOs)
                .message("Transaction history retrieved successfully")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/{amount}")
    public ResponseEntity<TransactionVM> searchByAmount(@PathVariable double amount) {
        List<TransactionDTO> transactions = transactionService.searchTransactionsByAmount(amount);
        TransactionVM response = TransactionVM.builder()
                .transactions(transactions)
                .message("Transactions found successfully for amount: " + amount)
                .statusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(response);
    }
    @GetMapping("/search")
    public ResponseEntity<List<TransactionDocument>> searchTransactions(
            @RequestParam(required = false, defaultValue = "0") double amount,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) TransactionStatus status) {

        List<TransactionDocument> transactions = transactionService.searchTransactions(amount, type, status);
        return ResponseEntity.ok(transactions);
    }


}
