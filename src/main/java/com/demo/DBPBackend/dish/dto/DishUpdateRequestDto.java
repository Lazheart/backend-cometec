package com.demo.DBPBackend.dish.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishUpdateRequestDto {
    private String name;
    private String description;
    private Double price;
}
