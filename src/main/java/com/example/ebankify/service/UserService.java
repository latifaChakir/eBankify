package com.example.ebankify.service;

import com.example.ebankify.domain.dtos.UserDto;
import com.example.ebankify.domain.entities.User;
import com.example.ebankify.domain.enums.Role;
import com.example.ebankify.domain.requests.LoginRequest;
import com.example.ebankify.domain.requests.RegisterRequest;
import com.example.ebankify.domain.requests.UserRequest;
import com.example.ebankify.exception.EmailAlreadyInUseException;
import com.example.ebankify.exception.UserNotFoundException;
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
            throw new EmailAlreadyInUseException("Email already in use");
        }

        User user = User.builder()
                .name(registerRequest.getName())
                .age(registerRequest.getAge())
                .email(registerRequest.getEmail())
                .active(registerRequest.isActive())
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
            user.setRole(Role.valueOf(String.valueOf(userRequest.getRole()).toUpperCase()));
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