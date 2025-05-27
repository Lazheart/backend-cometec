package com.demo.DBPBackend.restaurant.dto;

import com.demo.DBPBackend.ubicacion.dto.UbicacionDto;
import lombok.Data;

@Data
public class RestaurantSummaryDto {
    private Long id;
    private String name;
    private String ownerName;
    private UbicacionDto ubicacion;
    private Integer totalReviews;
}
