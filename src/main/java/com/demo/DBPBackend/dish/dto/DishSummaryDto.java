package com.demo.DBPBackend.dish.dto;

import lombok.Data;

@Data
public class DishSummaryDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
}
