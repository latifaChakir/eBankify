package com.example.ebankify.controller;

import com.example.ebankify.domain.dtos.AccountDTO;
import com.example.ebankify.domain.requests.AccountRequest;
import com.example.ebankify.domain.vm.AccountVM;
import com.example.ebankify.service.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class AccountController {
    private AccountService accountService;
    @PostMapping("/save")
    public ResponseEntity<AccountVM> save(@Valid @RequestBody AccountRequest accountRequest) {
        AccountDTO accountDTO = accountService.createAccount(accountRequest);
        AccountVM accountVM = AccountVM.builder()
                .account(accountDTO)
                .message("Account created successfully")
                .statusCode(201)
                .build();
        return ResponseEntity.status(201).body(accountVM);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AccountVM> getAccountById(@PathVariable Long id) {
        AccountDTO accountDTO = accountService.getAccountById(id);
        AccountVM accountVM = AccountVM.builder()
                .account(accountDTO)
                .message("Account retrieved successfully")
                .statusCode(200)
                .build();
        return ResponseEntity.ok(accountVM);
    }
    @PutMapping("/{id}")
    public ResponseEntity<AccountVM> updateAccount(@PathVariable Long id, @Valid @RequestBody AccountRequest accountRequest) {
        AccountDTO accountDTO = accountService.updateAccount(id, accountRequest);
        AccountVM accountVM = AccountVM.builder()
                .account(accountDTO)
                .message("Account updated successfully")
                .statusCode(200)
                .build();
        return ResponseEntity.ok(accountVM);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<AccountVM> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        AccountVM accountVM = AccountVM.builder()
                .message("Account deleted successfully")
                .statusCode(204)
                .build();
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/all")
    public ResponseEntity<AccountVM> getAllAccounts() {
        List<AccountDTO> accountDTOs = accountService.findAll();
        AccountVM accountVM = AccountVM.builder()
                .accounts(accountDTOs)
                .message("All accounts retrieved successfully")
                .statusCode(200)
                .build();
        return ResponseEntity.ok(accountVM);
    }

}
