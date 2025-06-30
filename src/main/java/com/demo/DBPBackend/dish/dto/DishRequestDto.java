package com.demo.DBPBackend.dish.dto;

import com.demo.DBPBackend.dish.domain.DishCategory;
import lombok.Data;

@Data
public class DishRequestDto {
    private String name;
    private Double price;
    private String description;
    private DishCategory category;
    private Long menuId;

}
