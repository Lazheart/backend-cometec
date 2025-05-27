package com.demo.DBPBackend.dish.dto;

import lombok.Data;

@Data
public class DishResponseDto {
    private Long DishId;
    private String name;
    private Double price;
    private Long menuId;
}
