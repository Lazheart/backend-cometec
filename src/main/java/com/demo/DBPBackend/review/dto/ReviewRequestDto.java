package com.demo.DBPBackend.review.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReviewRequestDto {
    private Long userId;
    private Long restaurantId;
    private String content;
}
