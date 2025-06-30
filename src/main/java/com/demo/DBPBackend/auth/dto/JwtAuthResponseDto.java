package com.demo.DBPBackend.auth.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthResponseDto {
    private String token;
}
