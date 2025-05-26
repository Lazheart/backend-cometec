package com.demo.DBPBackend.restaurante.domain;

import com.demo.DBPBackend.carta.domain.Carta;
import com.demo.DBPBackend.propietario.domain.Propietario;
import com.demo.DBPBackend.ubicacion.domain.Ubicacion;
import com.demo.DBPBackend.valoracion.domain.Valoracion;
import jakarta.persistence.*;

import java.security.DrbgParameters;
import java.util.List;

@Entity
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Consideras el nombre unico:
    // Rest1 - Distrito A
    // Rest1 - Distrito B
    @Column(length = 64, nullable = false, unique = true)
    private String nombreRestaurante;


    @ManyToOne
    @JoinColumn(name = "propietarioId")
    private Propietario propietario;

    @OneToOne
    private Ubicacion ubicacion;

    @OneToOne(mappedBy = "restaurante", cascade = CascadeType.ALL)
    private Carta  carta;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL)
    private List<Valoracion> valoraciones;




}
