package com.demo.DBPBackend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequestDto {
    @NotBlank
    private String code;

    @NotBlank
    private String newPassword;
}
