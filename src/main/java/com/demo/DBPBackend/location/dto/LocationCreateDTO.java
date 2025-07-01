package com.demo.DBPBackend.location.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationCreateDTO {
    private Double latitud;

    private Double longitud;
}
