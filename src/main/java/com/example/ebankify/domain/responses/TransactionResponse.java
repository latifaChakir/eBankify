package com.example.ebankify.domain.responses;

import com.example.ebankify.domain.dtos.TransactionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private TransactionDTO transactionDTO;
    private String message;
    private int statusCode;
    private List<TransactionDTO> transactions;
}
