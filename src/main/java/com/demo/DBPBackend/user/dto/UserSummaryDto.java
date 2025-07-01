package com.demo.DBPBackend.user.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserSummaryDto {
    private Long id;
    private String name;
    private String lastname;
}
