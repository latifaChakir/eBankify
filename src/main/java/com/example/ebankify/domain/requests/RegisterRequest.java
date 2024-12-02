package com.example.ebankify.domain.requests;

import com.example.ebankify.domain.enums.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = " name is required")
    private String name;
    @Min(value = 18, message = "Age must be at least 18")
    private int age;
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    @NotNull(message = "Le rôle ne peut pas être nul")
    private Role role;
    @NotNull(message = "Le statut d'activation ne peut pas être null")
    private boolean active;

}
