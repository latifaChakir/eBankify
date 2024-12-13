package com.example.ebankify.service;

import com.example.ebankify.domain.dtos.UserAuthDto;
import com.example.ebankify.domain.dtos.UserDto;
import com.example.ebankify.domain.entities.Role;
import com.example.ebankify.domain.entities.User;
import com.example.ebankify.domain.requests.LoginRequest;
import com.example.ebankify.domain.requests.RegisterRequest;
import com.example.ebankify.domain.requests.UserRequest;
import com.example.ebankify.exception.EmailAlreadyInUseException;
import com.example.ebankify.exception.InvalidCredentialsException;
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

    public UserAuthDto register(RegisterRequest registerRequest) {
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
                    .roles(new HashSet<>())
                    .build();

            Set<Role> roles = registerRequest.getRoles().stream()
                    .distinct()
                    .map(roleId -> roleRepository.findById(roleId)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + roleId)))
                    .collect(Collectors.toSet());

            user.getRoles().addAll(roles);

            User savedUser = userRepository.save(user);
            String token = jwtService.generateToken(savedUser, savedUser.getId());
            UserAuthDto userDto = userMapper.toUserAuthDto(savedUser);
            userDto.setToken(token);

            return userDto;
        } catch (Exception e) {
            System.out.println("Error during registration: " + e.getMessage());
            throw new RuntimeException("Registration failed", e);
        }
    }
    public UserAuthDto login(LoginRequest loginRequest)  {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }
        String token = jwtService.generateToken(user, user.getId());
        UserAuthDto userDto = userMapper.toUserAuthDto(user);
        userDto.setToken(token);
        return userDto;
    }

    public UserDto save(UserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email already in use");
        }
        if (userRequest.getRoles() == null || userRequest.getRoles().isEmpty()) {
            throw new RuntimeException("Roles cannot be null or empty");
        }
        Set<Role> roles = userRequest.getRoles().stream()
                .distinct()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleId)))
                .collect(Collectors.toSet());

        User user = userMapper.toEntity(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setActive(true);

        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().addAll(roles);

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