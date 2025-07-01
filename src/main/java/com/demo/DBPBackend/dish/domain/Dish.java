package com.demo.DBPBackend.dish.domain;

import com.demo.DBPBackend.menu.domain.Menu;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String description;

    @NotNull
    @DecimalMin("0.00")
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DishCategory category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private String imageUrl;
}