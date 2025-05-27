package com.demo.DBPBackend.dish.dto;

import lombok.Data;

@Data
public class DishUpdateRequestDto {
    private String name;
    private String description;
    private Double price;
}
