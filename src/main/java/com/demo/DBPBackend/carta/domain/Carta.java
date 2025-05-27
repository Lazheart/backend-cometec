package com.demo.DBPBackend.carta.domain;

import com.demo.DBPBackend.plato.domain.Plato;
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
public class Carta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarta;

    @Column(nullable = false, length = 128)
    private String nombre;

    @OneToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "carta", cascade = CascadeType.ALL)
    private List<Plato> platos;
}
