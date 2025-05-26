package com.demo.DBPBackend.carta.domain;

import com.demo.DBPBackend.restaurante.domain.Restaurante;
import jakarta.persistence.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDate;

@Entity
public class Carta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarta;

    @Column(nullable = false, length = 128)
    private String nombre;

    @OneToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;

    //Considerar si es necesario agregar una seccion de platos
}
