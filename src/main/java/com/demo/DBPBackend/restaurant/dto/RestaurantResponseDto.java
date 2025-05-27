package com.demo.DBPBackend.restaurant.dto;

import com.demo.DBPBackend.menu.domain.Menu;
import com.demo.DBPBackend.review.dto.ReviewResponseDto;
import com.demo.DBPBackend.ubicacion.domain.Ubicacion;
import com.demo.DBPBackend.ubicacion.dto.UbicacionDto;
import com.demo.DBPBackend.user.dto.UserSummaryDto;
import lombok.Data;

import java.util.List;

@Data
public class RestaurantResponseDto {
    private Long id;
    private String name;
    private Long ownerId;
    private String ownerName;
    private UbicacionDto ubicacion;
    private Integer totalReviews;
    private Boolean hasMenu;
}
