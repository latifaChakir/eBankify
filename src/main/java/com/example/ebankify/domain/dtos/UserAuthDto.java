package com.example.ebankify.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAuthDto {
    private Long id;
    private String name;
    private int age;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private double monthlyIncome;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int creditScore;
    private boolean active;
    private String token;
}
