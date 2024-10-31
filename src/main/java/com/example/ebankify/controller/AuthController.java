package com.example.ebankify.controller;

import com.example.ebankify.domain.dtos.UserDto;
import com.example.ebankify.domain.requests.LoginRequest;
import com.example.ebankify.domain.requests.RegisterRequest;
import com.example.ebankify.domain.responses.UserResponse;
import com.example.ebankify.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:8080")
@AllArgsConstructor
public class AuthController {
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
