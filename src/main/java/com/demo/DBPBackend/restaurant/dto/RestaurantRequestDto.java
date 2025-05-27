package com.demo.DBPBackend.restaurant.dto;

import com.demo.DBPBackend.ubicacion.dto.UbicacionDto;
import lombok.Data;

@Data
public class RestaurantRequestDto {
    private String name;

    private UbicacionDto ubicacion;
}
