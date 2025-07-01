package com.demo.DBPBackend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSecurityUpdateDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String securityCode;

    @NotBlank
    private String newPassword;
} 