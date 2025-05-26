package com.demo.DBPBackend.plato.domain;

import com.demo.DBPBackend.carta.domain.Carta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Plato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlato;

    private String nombre;
    private Double precio;
    private Boolean disponibilidad;

    @ManyToOne
    @JoinColumn(name = "carta_id")
    private Carta carta;
}