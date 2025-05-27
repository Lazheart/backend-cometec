package com.demo.DBPBackend.restaurant.domain;

import com.demo.DBPBackend.carta.domain.Carta;
import com.demo.DBPBackend.propietario.domain.Propietario;
import com.demo.DBPBackend.ubicacion.domain.Ubicacion;
import com.demo.DBPBackend.review.domain.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Restaurant {

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
    private List<Review> valoraciones;




}
