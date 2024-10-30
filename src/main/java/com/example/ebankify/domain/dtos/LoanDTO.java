package com.example.ebankify.domain.dtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanDTO {
    private Long id;
    private double principal;
    private double interestRate;
    private int termMonths;
    private boolean approved;
    private Long userId;
}