package com.demo.DBPBackend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyRecoveryCodeRequestDto {
    @NotBlank
    private String code;
}
