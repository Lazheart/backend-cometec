package com.demo.DBPBackend.restaurant.dto;

import com.demo.DBPBackend.location.dto.LocationDto;
import com.demo.DBPBackend.restaurant.domain.RestaurantCategory;
import lombok.Data;

@Data
public class RestaurantRequestDto {
    private String name;
    private RestaurantCategory category;
    private LocationDto locationDto;
}
