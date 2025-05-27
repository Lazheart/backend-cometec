package com.demo.DBPBackend.ubicacion.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Latitude is required")
    @Column(nullable = false)
    private double latitud;

    @NotBlank(message = "Latitude is required")
    @Column(nullable = false)
    private double longitud;

}
