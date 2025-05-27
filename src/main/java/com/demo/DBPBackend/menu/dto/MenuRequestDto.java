package com.demo.DBPBackend.menu.dto;

import com.demo.DBPBackend.dish.dto.DishRequestDto;
import lombok.Data;

import java.util.List;

@Data
public class MenuRequestDto {
    private Long restaurantId;
    private List<DishRequestDto> dishes;
}
