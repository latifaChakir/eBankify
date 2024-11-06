package com.example.ebankify.controller;

import com.example.ebankify.domain.dtos.LoanDTO;
import com.example.ebankify.domain.requests.LoanRequest;
import com.example.ebankify.domain.responses.LoanResponse;
import com.example.ebankify.service.LoanService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "http://localhost:8080")
@AllArgsConstructor
public class LoanController {
    private LoanService loanService;
    @PostMapping("/save")
    public ResponseEntity<LoanResponse> saveLoan(@Valid @RequestBody LoanRequest loanRequest){
        LoanDTO loanDTO = loanService.saveLoan(loanRequest);
        LoanResponse loanResponse = LoanResponse.builder()
                .loan(loanDTO)
                .message("Loan created successfully")
                .statusCode(201)
                .build();
        return ResponseEntity.ok(loanResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> getLoanById(@PathVariable Long id){
        LoanDTO loanDTO = loanService.getLoanById(id);
        LoanResponse loanResponse = LoanResponse.builder()
               .loan(loanDTO)
               .message("Loan found successfully")
               .statusCode(200)
               .build();
        return ResponseEntity.ok(loanResponse);
    }
    @DeleteMapping("/id")
    public ResponseEntity<LoanResponse> deleteLoan(@PathVariable Long id){
        loanService.deleteLoan(id);
        LoanResponse loanResponse = LoanResponse.builder()
               .message("Loan deleted successfully")
               .statusCode(204)
               .build();
        return ResponseEntity.ok(loanResponse);
    }
    @GetMapping
    public ResponseEntity<LoanResponse> getAllLoans(){
        List<LoanDTO> loanDTOs = loanService.findAll();
        LoanResponse loanResponse = LoanResponse.builder()
               .loans(loanDTOs)
               .message("Loans retrieved successfully")
               .statusCode(200)
               .build();
        return ResponseEntity.ok(loanResponse);
    }

}
