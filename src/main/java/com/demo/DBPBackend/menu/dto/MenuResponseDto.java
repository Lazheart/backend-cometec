package com.demo.DBPBackend.menu.dto;

import com.demo.DBPBackend.dish.dto.DishSummaryDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuResponseDto {
    private Long id;
    private Long restaurantId;
    private String restaurantName;
    private List<DishSummaryDto> dishes;
}
