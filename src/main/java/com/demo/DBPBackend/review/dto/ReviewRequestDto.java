package com.demo.DBPBackend.review.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewRequestDto {
    private Long userId;
    private Long restaurantId;
    private String content;
    private Integer rating;
}
