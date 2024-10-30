package com.example.ebankify.domain.responses;

import com.example.ebankify.domain.dtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UserDto user;
    private String message;
    private int statusCode;
}
