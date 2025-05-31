package com.demo.DBPBackend.restaurant.dto;

import com.demo.DBPBackend.location.dto.LocationDto;
import lombok.Data;

@Data
public class RestaurantSummaryDto {
    private Long id;
    private String name;
    private String ownerName;
    private LocationDto locationDto;
    private Integer totalReviews;
}
