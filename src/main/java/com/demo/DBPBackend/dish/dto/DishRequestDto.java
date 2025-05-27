package com.demo.DBPBackend.dish.dto;

import lombok.Data;

@Data
public class DishRequestDto {
    private String name;
    private Double price;
    private String description;
    private Long menuId;

}
