package com.example.ebankify.domain.responses;

import com.example.ebankify.domain.dtos.AccountDTO;
import com.example.ebankify.domain.dtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private AccountDTO account;
    private String message;
    private int statusCode;
    private List<AccountDTO> accounts;
}
