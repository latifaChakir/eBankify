package com.example.ebankify.domain.vm;

import com.example.ebankify.domain.dtos.AccountDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountVM {
    private AccountDTO account;
    private String message;
    private int statusCode;
    private List<AccountDTO> accounts;
}
