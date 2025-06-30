package com.demo.DBPBackend.dish.dto;

import com.demo.DBPBackend.dish.domain.DishCategory;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DishResponseDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private DishCategory category;
    private Long menuId;
}
