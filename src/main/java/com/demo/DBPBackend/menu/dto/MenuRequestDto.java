package com.demo.DBPBackend.menu.dto;

import com.demo.DBPBackend.dish.dto.DishRequestDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuRequestDto {
    private Long restaurantId;
    private List<DishRequestDto> dishes;
}
