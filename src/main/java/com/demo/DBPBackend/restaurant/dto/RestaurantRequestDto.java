package com.demo.DBPBackend.restaurant.dto;

import com.demo.DBPBackend.location.dto.LocationDto;
import lombok.Data;

@Data
public class RestaurantRequestDto {
    private String name;

    private LocationDto ubicacion;
}
