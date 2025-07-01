package com.demo.DBPBackend.location.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class GoogleMapsLocationDto {
    
    private Long id;
    private Double latitud;
    private Double longitud;
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantCategory;
    private Double averageRating;
    private String imageUrl;
    
    // Constructor para facilitar la creación
    public GoogleMapsLocationDto(Long id, Double latitud, Double longitud, String restaurantName, 
                                String restaurantAddress, String restaurantCategory, 
                                Double averageRating, String imageUrl) {
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantCategory = restaurantCategory;
        this.averageRating = averageRating;
        this.imageUrl = imageUrl;
    }
} 