package com.demo.DBPBackend.restaurant.dto;

import com.demo.DBPBackend.menu.domain.Menu;
import com.demo.DBPBackend.review.dto.ReviewResponseDto;
import com.demo.DBPBackend.ubicacion.domain.Ubicacion;
import com.demo.DBPBackend.user.dto.UserSummaryDto;
import lombok.Data;

import java.util.List;

@Data
public class RestaurantResponseDto {

    private String nombre;
    private Ubicacion ubicacion;
    private Menu menu;
    private List<ReviewResponseDto> reviews;
    private UserSummaryDto owner;

}
