package com.demo.DBPBackend.auth;

import com.demo.DBPBackend.auth.domain.AuthService;
import com.demo.DBPBackend.auth.dto.JwtAuthResponseDto;
import com.demo.DBPBackend.auth.dto.LoginDto;
import com.demo.DBPBackend.auth.dto.RegisterDto;
import com.demo.DBPBackend.conf.JwtService;
import com.demo.DBPBackend.events.register.RegisterEvent;
import com.demo.DBPBackend.exceptions.UserAlreadyExistException;
import com.demo.DBPBackend.user.domain.Role;
import com.demo.DBPBackend.user.domain.User;
import com.demo.DBPBackend.user.infrastructure.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AuthService authService;

    private User user;
    private RegisterDto registerDto;
    private LoginDto loginDto;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setName("Test");
        user.setLastname("User");
        user.setPhone("123456789");
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());

        registerDto = new RegisterDto();
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("password");
        registerDto.setName("Test");
        registerDto.setLastname("User");
        registerDto.setPhone("123456789");
        registerDto.setRole(Role.USER);

        loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");
    }

    @Test
    public void register_NewUser_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(userRepository.save(any(User.class))).thenReturn(user);

        JwtAuthResponseDto response = authService.register(registerDto);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(eventPublisher).publishEvent(any(RegisterEvent.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void register_ExistingUser_ThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(user));

        assertThrows(UserAlreadyExistException.class, () -> {
            authService.register(registerDto);
        });
    }

    @Test
    public void login_ValidCredentials_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        JwtAuthResponseDto response = authService.login(loginDto);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
    }

    @Test
    public void login_InvalidEmail_ThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.login(loginDto);
        });
    }

    @Test
    public void login_InvalidPassword_ThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            authService.login(loginDto);
        });
    }

    @Test
    public void verifyPassword_ValidPassword_ReturnsTrue() {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        boolean result = authService.verifyPassword(1L, "password");

        assertTrue(result);
    }

    @Test
    public void verifyPassword_InvalidPassword_ReturnsFalse() {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        boolean result = authService.verifyPassword(1L, "wrongPassword");

        assertFalse(result);
    }

    @Test
    public void verifyPassword_UserNotFound_ThrowsException() {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.verifyPassword(1L, "password");
        });
    }
}