package com.demo.DBPBackend.dish.dto;

import com.demo.DBPBackend.dish.domain.DishCategory;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishRequestDto {
    private String name;
    private Double price;
    private String description;
    private DishCategory category;
    private Long menuId;

}
