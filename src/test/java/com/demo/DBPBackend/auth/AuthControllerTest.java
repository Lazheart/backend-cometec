package com.demo.DBPBackend.auth;

import com.demo.DBPBackend.auth.application.AuthController;
import com.demo.DBPBackend.auth.domain.AuthService;
import com.demo.DBPBackend.auth.dto.JwtAuthResponseDto;
import com.demo.DBPBackend.auth.dto.LoginDto;
import com.demo.DBPBackend.auth.dto.RegisterDto;
import com.demo.DBPBackend.auth.dto.UserPasswordVerificationRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private LoginDto loginDto;
    private RegisterDto registerDto;
    private UserPasswordVerificationRequestDto passwordVerificationDto;
    private JwtAuthResponseDto jwtResponse;

    @BeforeEach
    public void setUp() {
        loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");

        registerDto = new RegisterDto();
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("password");
        registerDto.setName("Test");
        registerDto.setLastname("User");
        registerDto.setPhone("123456789");

        passwordVerificationDto = new UserPasswordVerificationRequestDto();
        passwordVerificationDto.setUserId(1L);
        passwordVerificationDto.setPassword("password");

        jwtResponse = new JwtAuthResponseDto();
        jwtResponse.setToken("jwtToken");
    }

    @Test
    public void login_ValidCredentials_ReturnsToken() {
        when(authService.login(any(LoginDto.class))).thenReturn(jwtResponse);

        ResponseEntity<JwtAuthResponseDto> response = authController.login(loginDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("jwtToken", response.getBody().getToken());
    }

    @Test
    public void register_NewUser_ReturnsToken() {
        when(authService.register(any(RegisterDto.class))).thenReturn(jwtResponse);

        ResponseEntity<JwtAuthResponseDto> response = authController.register(registerDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("jwtToken", response.getBody().getToken());
    }

    @Test
    public void verifyPassword_ValidPassword_ReturnsTrue() {
        when(authService.verifyPassword(anyLong(), anyString())).thenReturn(true);

        ResponseEntity<Boolean> response = authController.verifyPassword(passwordVerificationDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
    }

    @Test
    public void verifyPassword_InvalidPassword_ReturnsFalse() {
        when(authService.verifyPassword(anyLong(), anyString())).thenReturn(false);

        ResponseEntity<Boolean> response = authController.verifyPassword(passwordVerificationDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody());
    }
}