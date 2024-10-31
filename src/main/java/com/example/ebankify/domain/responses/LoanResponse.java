package com.example.ebankify.domain.responses;

import com.example.ebankify.domain.dtos.LoanDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanResponse {
    private LoanDTO loanDTO;
    private String message;
    private int statusCode;
}
