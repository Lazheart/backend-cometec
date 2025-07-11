package com.demo.DBPBackend.restaurant.dto;

import com.demo.DBPBackend.review.dto.ReviewResponseDto;
import com.demo.DBPBackend.restaurant.domain.RestaurantCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantWithReviewsDto {
    private Long id;
    private String name;
    private RestaurantCategory category;
    private List<ReviewResponseDto> reviews;
}

