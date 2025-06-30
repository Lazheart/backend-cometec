package com.demo.DBPBackend.auth.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordVerificationRequestDto {
    private Long userId;
    private String password;
}
