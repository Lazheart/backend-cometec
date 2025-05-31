package com.demo.DBPBackend.restaurant.dto;

import com.demo.DBPBackend.location.dto.LocationDto;
import lombok.Data;

@Data
public class RestaurantResponseDto {
    private Long id;
    private String name;
    private Long ownerId;
    private String ownerName;
    private LocationDto locationDto;
    private Integer totalReviews;
    private Boolean hasMenu;
}
