package com.demo.DBPBackend.user.dto;

import com.demo.DBPBackend.restaurant.dto.RestaurantSummaryDto;
import com.demo.DBPBackend.review.dto.ReviewResponseDto;
import com.demo.DBPBackend.user.domain.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String phone;
    private String profileImageUrl;
    private Role role;
    private LocalDateTime createdAt;
    private List<ReviewResponseDto> reviews;
    private List<RestaurantSummaryDto> favouriteRestaurants;
}
