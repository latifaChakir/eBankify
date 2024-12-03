package com.example.ebankify.controller;

import com.example.ebankify.domain.dtos.UserAuthDto;
import com.example.ebankify.domain.dtos.UserDto;
import com.example.ebankify.domain.requests.LoginRequest;
import com.example.ebankify.domain.requests.RegisterRequest;
import com.example.ebankify.domain.vm.UserAuthVm;
import com.example.ebankify.domain.vm.UserVM;
import com.example.ebankify.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class AuthController {
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<UserAuthVm> register(@Valid @RequestBody RegisterRequest registerRequest) {
        UserAuthDto userDto = userService.register(registerRequest);
        UserAuthVm response = UserAuthVm.builder()
                .user(userDto)
                .message("Registration successful")
                .statusCode(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthVm> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserAuthDto userDto = userService.login(loginRequest);
        UserAuthVm response = UserAuthVm.builder()
                .user(userDto)
                .message("Login successful ")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
}
