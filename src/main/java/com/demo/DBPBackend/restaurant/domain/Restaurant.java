package com.demo.DBPBackend.restaurant.domain;

import com.demo.DBPBackend.menu.domain.Menu;
import com.demo.DBPBackend.ubicacion.domain.Ubicacion;
import com.demo.DBPBackend.review.domain.Review;
import com.demo.DBPBackend.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Consideras el nombre unico:
    // Rest1 - Distrito A
    // Rest1 - Distrito B
    @Column(length = 64, nullable = false, unique = true)
    @NotBlank
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @OneToOne(mappedBy = "restaurante", cascade = CascadeType.ALL)
    private Menu menu;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL)
    private List<Review> valoraciones;

    @ManyToMany(mappedBy = "favouriteRestaurants")
    private List<User> favouritedBy;

}
