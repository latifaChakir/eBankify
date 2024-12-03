package com.example.ebankify.service;

import com.example.ebankify.domain.dtos.UserDto;
import com.example.ebankify.domain.entities.Role;
import com.example.ebankify.domain.entities.User;
import com.example.ebankify.domain.requests.LoginRequest;
import com.example.ebankify.domain.requests.RegisterRequest;
import com.example.ebankify.domain.requests.UserRequest;
import com.example.ebankify.exception.EmailAlreadyInUseException;
import com.example.ebankify.exception.UserNotFoundException;
import com.example.ebankify.mapper.UserMapper;
import com.example.ebankify.repository.RoleRepository;
import com.example.ebankify.repository.UserRepository;
import com.example.ebankify.security.JwtService;
import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;

    public UserDto register(RegisterRequest registerRequest) {
        try {
            System.out.println("Registering user: " + registerRequest);
            if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
                throw new EmailAlreadyInUseException("Email already in use");
            }

            User user = User.builder()
                    .name(registerRequest.getName())
                    .age(registerRequest.getAge())
                    .email(registerRequest.getEmail())
                    .active(registerRequest.isActive())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .roles(new HashSet<>()) // Ensure roles is initialized
                    .build();

            Set<Role> roles = registerRequest.getRoles().stream()
                    .distinct()
                    .map(roleId -> roleRepository.findById(roleId)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + roleId)))
                    .collect(Collectors.toSet());

            user.getRoles().addAll(roles); // Now this should work without throwing NullPointerException

            User savedUser = userRepository.save(user);
            String token = jwtService.generateToken(savedUser, savedUser.getId());
            UserDto userDto = userMapper.toDto(savedUser);
            userDto.setToken(token);

            return userDto;
        } catch (Exception e) {
            System.out.println("Error during registration: " + e.getMessage());
            throw new RuntimeException("Registration failed", e); // Use custom exception
        }
    }
    public UserDto login(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userOptional.get();
        if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return userMapper.toDto(user);
    }

    public UserDto save(UserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email already in use");
        }
        userRequest.setPassword(BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt()));
        userRequest.setActive(true);
        User user = userMapper.toEntity(userRequest);
        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    public UserDto findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        return userMapper.toDto(userOptional.get());
    }

    public void deleteById(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        System.out.println("id pour supprimer "+id);
        userRepository.deleteById(id);
    }

    public UserDto update(Long id, UserRequest userRequest) {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()) {
                throw new UserNotFoundException("User not found");
            }
            User user = userOptional.get();
            user.setName(userRequest.getName());
            user.setAge(userRequest.getAge());
            user.setEmail(userRequest.getEmail());
            user.setActive(userRequest.isActive());
            user.setMonthlyIncome(userRequest.getMonthlyIncome());
            user.setCreditScore(userRequest.getCreditScore());
            if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
                user.setPassword(BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt()));
            }
            User savedUser = userRepository.save(user);
            return userMapper.toDto(savedUser);
    }

    public void blockUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        User user = userOptional.get();
        user.setActive(false);
        userRepository.save(user);
    }

    public void unblockUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        User user = userOptional.get();
        user.setActive(true);
        userRepository.save(user);
    }
}