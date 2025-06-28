package com.demo.DBPBackend.menu.dto;

import com.demo.DBPBackend.dish.dto.DishSummaryDto;
import lombok.Data;

import java.util.List;

@Data
public class MenuResponseDto {
    private Long id;
    private Long restaurantId;
    private String restaurantName;
    private List<DishSummaryDto> dishes;
}
