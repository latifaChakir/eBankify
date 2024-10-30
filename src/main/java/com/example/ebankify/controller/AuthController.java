package com.example.ebankify.controller;

import com.example.ebankify.domain.dtos.UserDto;
import com.example.ebankify.domain.requests.LoginRequest;
import com.example.ebankify.domain.requests.RegisterRequest;
import com.example.ebankify.domain.responses.UserResponse;
import com.example.ebankify.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        UserDto userDto = userService.register(registerRequest);
        UserResponse response = UserResponse.builder()
                .user(userDto)
                .message("Registration successful")
                .statusCode(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserDto userDto = userService.login(loginRequest);
        UserResponse response = UserResponse.builder()
                .user(userDto)
                .message("Login successful")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
}
