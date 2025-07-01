package com.demo.DBPBackend.location.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDto {

    private Long id;

    private Double latitud;

    private Double longitud;

    private Long restaurantId;

    private String restaurantName;
}
