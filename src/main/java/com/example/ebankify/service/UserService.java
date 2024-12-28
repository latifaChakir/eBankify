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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    public UserAuthDto register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email already in use");
        }

        // Créez l'utilisateur et effectuez d'autres vérifications
        User user = User.builder()
                .name(registerRequest.getName())
                .age(registerRequest.getAge())
                .email(registerRequest.getEmail())
                .active(registerRequest.isActive())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .roles(new HashSet<>()) // Initialize with an empty set
                .build();

        // Ajout des rôles et sauvegarde de l'utilisateur
        Set<Role> roles = registerRequest.getRoles().stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleId)))
                .collect(Collectors.toSet());

        user.getRoles().addAll(roles);

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser, savedUser.getId());
        UserAuthDto userDto = userMapper.toUserAuthDto(savedUser);
        userDto.setToken(token);

        return userDto;
    }
    public UserAuthDto login(LoginRequest loginRequest) {
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
        user.setRoles(new HashSet<>(roles));

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
    public List<UserDto> findAll(){
        List<User> users = userRepository.findAll();
        return userMapper.toDtoList(users);

    }

    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.getRoles().clear();
        userRepository.save(user); // Mettre à jour la table de jointure
        userRepository.deleteById(id);
        System.out.println("Utilisateur avec l'ID " + id + " supprimé avec succès.");
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
    public UserAuthDto getCurrentUser(String token) {
        Long userId = jwtService.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toUserAuthDto(user);
    }
}