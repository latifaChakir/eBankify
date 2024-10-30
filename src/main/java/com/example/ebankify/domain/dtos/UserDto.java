package com.example.ebankify.domain.dtos;

import com.example.ebankify.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String name;
    private int age;
    private String email;
    private double monthlyIncome;
    private int creditScore;
    private Role role;
}
