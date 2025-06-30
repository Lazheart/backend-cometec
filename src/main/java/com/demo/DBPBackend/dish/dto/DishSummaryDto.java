package com.demo.DBPBackend.dish.dto;

import com.demo.DBPBackend.dish.domain.DishCategory;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishSummaryDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private DishCategory category;
}
