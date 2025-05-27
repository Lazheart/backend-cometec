package com.demo.DBPBackend.menu.domain;

import com.demo.DBPBackend.dish.domain.Dish;
import com.demo.DBPBackend.restaurant.domain.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarta;

    @OneToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "carta", cascade = CascadeType.ALL)
    private List<Dish> dishes;
}
