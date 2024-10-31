package com.example.ebankify.service;

import com.example.ebankify.domain.dtos.UserDto;
import com.example.ebankify.domain.entities.User;
import com.example.ebankify.domain.enums.Role;
import com.example.ebankify.domain.requests.LoginRequest;
import com.example.ebankify.domain.requests.RegisterRequest;
import com.example.ebankify.mapper.UserMapper;
import com.example.ebankify.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public UserDto register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .name(registerRequest.getName())
                .age(registerRequest.getAge())
                .email(registerRequest.getEmail())
                .password(BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt()))
                .monthlyIncome(registerRequest.getMonthlyIncome())
                .creditScore(registerRequest.getCreditScore())
                .role(Role.valueOf(String.valueOf(registerRequest.getRole()).toUpperCase()))
                .build();

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
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
}
