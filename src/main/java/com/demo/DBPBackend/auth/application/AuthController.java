package com.demo.DBPBackend.auth.application;

import com.demo.DBPBackend.auth.domain.AuthService;
import com.demo.DBPBackend.auth.dto.JwtAuthResponseDto;
import com.demo.DBPBackend.auth.dto.LoginDto;
import com.demo.DBPBackend.auth.dto.RegisterDto;
import com.demo.DBPBackend.auth.dto.UserPasswordVerificationRequestDto;
import com.demo.DBPBackend.auth.dto.RecoveryRequestDto;
import com.demo.DBPBackend.auth.dto.RecoveryResponseDto;
import com.demo.DBPBackend.auth.dto.ResetPasswordRequestDto;
import com.demo.DBPBackend.auth.dto.VerifyRecoveryCodeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDto> login(@RequestBody LoginDto logInDTO) {
        return ResponseEntity.ok(authService.login(logInDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtAuthResponseDto> register(@RequestBody RegisterDto registerDTO) {
        return ResponseEntity.ok(authService.register(registerDTO));
    }

    @PostMapping("/verify-password")
    public ResponseEntity<Boolean> verifyPassword(@RequestBody UserPasswordVerificationRequestDto request) {
        boolean isValid = authService.verifyPassword(request.getUserId(), request.getPassword());
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/recovery")
    public ResponseEntity<RecoveryResponseDto> recovery(@Valid @RequestBody RecoveryRequestDto dto) {
        return ResponseEntity.ok(authService.sendRecoveryCode(dto));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<RecoveryResponseDto> resetPassword(@RequestParam("email") String email, @Valid @RequestBody ResetPasswordRequestDto dto) {
        return ResponseEntity.ok(authService.resetPassword(email, dto));
    }

    @PostMapping("/verify-recovery-code")
    public ResponseEntity<Boolean> verifyRecoveryCode(@RequestParam("email") String email, @Valid @RequestBody VerifyRecoveryCodeRequestDto dto) {
        return ResponseEntity.ok(authService.verifyRecoveryCode(email, dto));
    }
}
