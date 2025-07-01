package com.demo.DBPBackend.user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    private String name;
    private String password;
    private String email;
    private String phone;
    private String lastname;
}
