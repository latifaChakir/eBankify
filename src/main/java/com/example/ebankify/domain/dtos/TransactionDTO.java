package com.example.ebankify.domain.dtos;

import com.example.ebankify.domain.entities.Account;
import com.example.ebankify.domain.enums.TransactionStatus;
import com.example.ebankify.domain.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {
    private Long id;
    private TransactionType type;
    private double amount;
    private TransactionStatus status;
    private AccountDTO sourceAccount;
    private AccountDTO destinationAccount;
}
