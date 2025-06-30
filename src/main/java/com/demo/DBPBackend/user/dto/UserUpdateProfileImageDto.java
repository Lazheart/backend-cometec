package com.demo.DBPBackend.user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserUpdateProfileImageDto {
    private MultipartFile profileImage;
} 