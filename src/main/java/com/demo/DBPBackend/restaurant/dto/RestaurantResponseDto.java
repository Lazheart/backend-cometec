package com.demo.DBPBackend.restaurant.dto;

import com.demo.DBPBackend.carta.domain.Carta;
import com.demo.DBPBackend.ubicacion.domain.Ubicacion;
import com.demo.DBPBackend.user.domain.User;
import lombok.Data;

import java.util.List;

@Data
public class RestaurantResponseDto {

    private String nombre;
    private Ubicacion ubicacion;
    private Carta carta;
    private List<ReviewResponseDto> reviews;
}
