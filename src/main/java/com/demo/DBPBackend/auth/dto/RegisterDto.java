package com.demo.DBPBackend.auth.dto;

import com.demo.DBPBackend.user.domain.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {
    private String email;
    private String password;
    private String name;
    private String lastname;
    private String phone;
    private Role role = Role.USER;
}
