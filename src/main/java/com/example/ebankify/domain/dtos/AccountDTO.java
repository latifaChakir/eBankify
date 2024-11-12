package com.example.ebankify.domain.dtos;

import com.example.ebankify.domain.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {
    private Long id;
    private double balance;
    private String accountNumber;
    private AccountStatus status;
    private UserDto user;
    private BankDto bank;
}