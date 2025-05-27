package com.demo.DBPBackend.auth.domain;

import com.demo.DBPBackend.auth.dto.JwtAuthResponseDto;
import com.demo.DBPBackend.auth.dto.LoginDto;
import com.demo.DBPBackend.auth.dto.RegisterDto;
import com.demo.DBPBackend.conf.JwtService;
import com.demo.DBPBackend.events.register.RegisterEvent;
import com.demo.DBPBackend.exceptions.UserAlreadyExistException;
import com.demo.DBPBackend.user.domain.User;
import com.demo.DBPBackend.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;


    public JwtAuthResponseDto login(LoginDto logInDto) {
        User user = userRepository.findByEmail(logInDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        if (!passwordEncoder.matches(logInDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        JwtAuthResponseDto response = new JwtAuthResponseDto();
        response.setToken(jwtService.generateToken(user));
        return response;
    }

    public JwtAuthResponseDto register(RegisterDto registerDto) {
        if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User already exist");
        }

        applicationEventPublisher.publishEvent(new RegisterEvent(registerDto.getEmail(), registerDto.getName()));

        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setName(registerDto.getName());
        user.setLastname(registerDto.getLastname());
        user.setPhone(registerDto.getPhone());
        user.setRole(registerDto.getRole());  // Usa el enum directamente
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
        JwtAuthResponseDto response = new JwtAuthResponseDto();
        response.setToken(jwtService.generateToken(user));
        return response;
    }

    public boolean verifyPassword(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return passwordEncoder.matches(password, user.getPassword());
    }
}
