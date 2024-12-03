package com.example.ebankify.controller;

import com.example.ebankify.domain.dtos.LoanDTO;
import com.example.ebankify.domain.requests.LoanRequest;
import com.example.ebankify.domain.vm.LoanVM;
import com.example.ebankify.service.LoanService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class LoanController {
    private LoanService loanService;
    @PostMapping("/save")
    public ResponseEntity<LoanVM> saveLoan(@Valid @RequestBody LoanRequest loanRequest){
        LoanDTO loanDTO = loanService.saveLoan(loanRequest);
        LoanVM loanVM = LoanVM.builder()
                .loan(loanDTO)
                .message("Loan created successfully")
                .statusCode(201)
                .build();
        return ResponseEntity.ok(loanVM);
    }
    @GetMapping("/{id}")
    public ResponseEntity<LoanVM> getLoanById(@PathVariable Long id){
        LoanDTO loanDTO = loanService.getLoanById(id);
        LoanVM loanVM = LoanVM.builder()
               .loan(loanDTO)
               .message("Loan found successfully")
               .statusCode(200)
               .build();
        return ResponseEntity.ok(loanVM);
    }
    @DeleteMapping("/id")
    public ResponseEntity<LoanVM> deleteLoan(@PathVariable Long id){
        loanService.deleteLoan(id);
        LoanVM loanVM = LoanVM.builder()
               .message("Loan deleted successfully")
               .statusCode(204)
               .build();
        return ResponseEntity.ok(loanVM);
    }
    @GetMapping
    public ResponseEntity<LoanVM> getAllLoans(){
        List<LoanDTO> loanDTOs = loanService.findAll();
        LoanVM loanVM = LoanVM.builder()
               .loans(loanDTOs)
               .message("Loans retrieved successfully")
               .statusCode(200)
               .build();
        return ResponseEntity.ok(loanVM);
    }

}
