package com.demo.DBPBackend.location.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class LocationSearchDto {
    
    private Double minLatitud;
    private Double maxLatitud;
    private Double minLongitud;
    private Double maxLongitud;
    private String category;
    private Double minRating;
    private Integer page = 0;
    private Integer size = 20;
    
    // Constructor por defecto
    public LocationSearchDto() {}
    
    // Constructor con parámetros principales
    public LocationSearchDto(Double minLatitud, Double maxLatitud, Double minLongitud, Double maxLongitud) {
        this.minLatitud = minLatitud;
        this.maxLatitud = maxLatitud;
        this.minLongitud = minLongitud;
        this.maxLongitud = maxLongitud;
    }
} 