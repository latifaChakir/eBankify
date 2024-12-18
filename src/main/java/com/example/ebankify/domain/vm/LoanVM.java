package com.example.ebankify.domain.vm;

import com.example.ebankify.domain.dtos.LoanDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanVM {
    private LoanDTO loan;
    private String message;
    private int statusCode;
    private List<LoanDTO> loans;
}
