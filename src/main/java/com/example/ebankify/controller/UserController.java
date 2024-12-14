package com.example.ebankify.controller;

import com.example.ebankify.domain.dtos.UserDto;
import com.example.ebankify.domain.requests.UserRequest;
import com.example.ebankify.domain.vm.UserVM;
import com.example.ebankify.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class UserController {
    private UserService userService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<UserVM> saveUser(@Valid @RequestBody UserRequest userRequest) {
        UserDto userDto=userService.save(userRequest);
        UserVM response = UserVM.builder()
                .user(userDto)
                .message("user saved successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserVM> getUserById (@PathVariable Long id) {
        UserDto userDto = userService.findById(id);
        UserVM response = UserVM.builder()
                .user(userDto)
                .message("user found successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<UserVM> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        UserDto userDto = userService.update(id, userRequest);
        UserVM response = UserVM.builder()
                .user(userDto)
                .message("user updated successfully")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserVM> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        UserVM response = UserVM.builder()
                .message("user deleted successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/block/{userId}")
    public ResponseEntity<UserVM> blockUser(@PathVariable Long userId) {
        userService.blockUser(userId);
        UserVM response = UserVM.builder()
                .message("user blocked successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/unblock/{userId}")
    public ResponseEntity<UserVM> unblockUser(@PathVariable Long userId) {
        userService.unblockUser(userId);
        UserVM response = UserVM.builder()
                .message("user unblocked successfuly")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
}
