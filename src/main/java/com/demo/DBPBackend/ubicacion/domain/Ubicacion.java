package com.demo.DBPBackend.ubicacion.domain;

import jakarta.persistence.*;

@Entity
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double latitud;

    private double longitud;

}
