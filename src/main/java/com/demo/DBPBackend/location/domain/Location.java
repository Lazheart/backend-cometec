package com.demo.DBPBackend.location.domain;

import com.demo.DBPBackend.restaurant.domain.Restaurant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private double latitud;

    @NotNull
    @Column(nullable = false)
    private double longitud;

    @OneToOne(mappedBy = "location")
    private Restaurant restaurant;

}
