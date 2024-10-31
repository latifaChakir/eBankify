package com.example.ebankify.controller;

import com.example.ebankify.domain.dtos.AccountDTO;
import com.example.ebankify.domain.requests.AccountRequest;
import com.example.ebankify.domain.responses.AccountResponse;
import com.example.ebankify.service.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "http://localhost:8080")
@AllArgsConstructor
public class AccountController {
    private AccountService accountService;
    @PostMapping("/save")
    public ResponseEntity<AccountResponse> save(@Valid @RequestBody AccountRequest accountRequest) {
        AccountDTO accountDTO = accountService.createAccount(accountRequest);
        AccountResponse accountResponse=AccountResponse.builder()
                .account(accountDTO)
                .message("Account created successfully")
                .statusCode(201)
                .build();
        return ResponseEntity.ok(accountResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        AccountDTO accountDTO = accountService.getAccountById(id);
        AccountResponse accountResponse=AccountResponse.builder()
                .account(accountDTO)
                .message("Account retrieved successfully")
                .statusCode(200)
                .build();
        return ResponseEntity.ok(accountResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable Long id, @Valid @RequestBody AccountRequest accountRequest) {
        AccountDTO accountDTO = accountService.updateAccount(id, accountRequest);
        AccountResponse accountResponse=AccountResponse.builder()
                .account(accountDTO)
                .message("Account updated successfully")
                .statusCode(200)
                .build();
        return ResponseEntity.ok(accountResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<AccountResponse> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        AccountResponse accountResponse=AccountResponse.builder()
                .message("Account deleted successfully")
                .statusCode(204)
                .build();
        return ResponseEntity.ok(accountResponse);
    }
    @GetMapping("/all")
    public ResponseEntity<AccountResponse> getAllAccounts() {
        List<AccountDTO> accountDTOs = accountService.findAll();
        AccountResponse accountResponse=AccountResponse.builder()
                .accounts(accountDTOs)
                .message("All accounts retrieved successfully")
                .statusCode(200)
                .build();
        return ResponseEntity.ok(accountResponse);
    }

}
