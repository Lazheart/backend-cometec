package com.demo.DBPBackend.plato.domain;

import com.demo.DBPBackend.carta.domain.Carta;
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
public class Plato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlato;

    @NotBlank
    private String nombre;

    @NotNull
    @DecimalMin("0.00")
    private Double precio;

    @NotNull
    private Boolean disponibilidad;


    @ManyToOne
    @JoinColumn(name = "carta_id")
    private Carta carta;
}