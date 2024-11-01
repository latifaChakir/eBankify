package com.example.ebankify.controller;

import com.example.ebankify.domain.dtos.UserDto;
import com.example.ebankify.domain.requests.UserRequest;
import com.example.ebankify.domain.responses.UserResponse;
import com.example.ebankify.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:8080")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping("/save")
    public ResponseEntity<UserResponse> saveUser(@Valid @RequestBody UserRequest userRequest) {
        UserDto userDto=userService.save(userRequest);
        UserResponse response = UserResponse.builder()
                .user(userDto)
                .message("user saved successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById (@PathVariable Long id) {
        UserDto userDto = userService.findById(id);
        UserResponse response = UserResponse.builder()
                .user(userDto)
                .message("user found successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        UserDto userDto = userService.update(id, userRequest);
        UserResponse response = UserResponse.builder()
                .user(userDto)
                .message("user updated successfully")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        UserResponse response = UserResponse.builder()
                .message("user deleted successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
}
