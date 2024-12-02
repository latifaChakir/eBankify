package com.example.ebankify;

import com.example.ebankify.domain.dtos.UserDto;
import com.example.ebankify.domain.entities.User;
import com.example.ebankify.domain.enums.Role;
import com.example.ebankify.domain.requests.LoginRequest;
import com.example.ebankify.domain.requests.RegisterRequest;
import com.example.ebankify.domain.requests.UserRequest;
import com.example.ebankify.exception.UserNotFoundException;
import com.example.ebankify.mapper.UserMapper;
import com.example.ebankify.repository.UserRepository;
import com.example.ebankify.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExists() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("existing@example.com");

        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class, () -> userService.register(registerRequest));
        verify(userRepository).findByEmail(registerRequest.getEmail());
    }
    @Test
    void register_ShouldReturnUserDto_WhenRegistrationIsSuccessful() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("John Doe");
        registerRequest.setAge(30);
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRole(Role.USER);
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        String hashedPassword = BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt());
        User savedUser = User.builder()
                .name(registerRequest.getName())
                .age(registerRequest.getAge())
                .email(registerRequest.getEmail())
                .password(hashedPassword)
                .build();

        when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(UserDto.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .age(savedUser.getAge())
                .email(savedUser.getEmail())
                .monthlyIncome(savedUser.getMonthlyIncome())
                .creditScore(savedUser.getCreditScore())
                .build());
        UserDto userDto = userService.register(registerRequest);
        verify(userRepository).save(Mockito.any(User.class));
        assertNotNull(userDto);
        assertEquals(registerRequest.getEmail(), userDto.getEmail());
        assertEquals(registerRequest.getName(), userDto.getName());
        assertTrue(BCrypt.checkpw(registerRequest.getPassword(), savedUser.getPassword()));
    }


    @Test
    void login_ShouldThrowException_WhenUserNotFound() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("unknown@example.com");
        loginRequest.setPassword("password");

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.login(loginRequest));
        verify(userRepository).findByEmail(loginRequest.getEmail());
    }

    @Test
    void login_ShouldReturnUserDto_WhenCredentialsAreValid() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("password");

        User user = User.builder()
                .email(loginRequest.getEmail())
                .password(BCrypt.hashpw(loginRequest.getPassword(), BCrypt.gensalt()))
                .build();

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(new UserDto());

        UserDto result = userService.login(loginRequest);

        assertNotNull(result);
        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(userMapper).toDto(user);
    }

    @Test
    void save_ShouldReturnUserDto_WhenSuccessful() {
        UserRequest userRequest = new UserRequest();
        User user = new User();
        User savedUser = new User();

        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(new UserDto());

        UserDto result = userService.save(userRequest);

        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    void findById_ShouldThrowUserNotFoundException_WhenUserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(userId));
        verify(userRepository).findById(userId);
    }

    @Test
    void findById_ShouldReturnUserDto_WhenUserIsFound() {
        Long userId = 1L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(new UserDto());

        UserDto result = userService.findById(userId);

        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(userMapper).toDto(user);
    }

    @Test
    void deleteById_ShouldThrowUserNotFoundException_WhenUserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteById(userId));
        verify(userRepository).findById(userId);
    }

    @Test
    void deleteById_ShouldDeleteUser_WhenUserIsFound() {
        Long userId = 1L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteById(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void update_ShouldThrowUserNotFoundException_WhenUserNotFound() {
        Long userId = 1L;
        UserRequest userRequest = new UserRequest();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.update(userId, userRequest));
        verify(userRepository).findById(userId);
    }

    @Test
    void update_ShouldReturnUpdatedUserDto_WhenUserIsFound() {
        Long userId = 1L;
        UserRequest userRequest = new UserRequest();
        userRequest.setRole(Role.USER); 
        userRequest.setName("Updated Name");
        User user = new User();
        User updatedUser = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(new UserDto());

        UserDto result = userService.update(userId, userRequest);

        assertNotNull(result);
        verify(userRepository).save(user);
        verify(userMapper).toDto(updatedUser);
    }
    @Test
    void update_ShouldReturnUpdatedUserDto_WhenUpdateIsSuccessful() {
        Long userId = 1L;
        UserRequest userRequest = new UserRequest();
        userRequest.setRole(Role.USER);

        User existingUser = new User();
        existingUser.setId(userId);

        User updatedUser = new User();
        updatedUser.setId(userId);

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setRole(Role.USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userMapper.toEntity(userRequest)).thenReturn(updatedUser);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(updatedUserDto);

        UserDto result = userService.update(userId, userRequest);

        assertNotNull(result);
        assertEquals(Role.USER, result.getRole()); // Vérifiez le rôle ici
        verify(userRepository).findById(userId);
        verify(userRepository).save(updatedUser);
        verify(userMapper).toDto(updatedUser);
    }

}
