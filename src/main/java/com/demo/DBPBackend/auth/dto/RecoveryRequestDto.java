package com.demo.DBPBackend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecoveryRequestDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String lastname;

    @NotBlank
    private String phone;
} 