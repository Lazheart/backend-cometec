package com.demo.DBPBackend.user.dto;

import com.demo.DBPBackend.restaurant.domain.Restaurant;
import com.demo.DBPBackend.review.domain.Review;
import com.demo.DBPBackend.user.domain.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private String phone;
    private List<Review> reviews;
    private List<Restaurant> favouriteRestaurants;

}
