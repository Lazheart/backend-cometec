package com.demo.DBPBackend.review.domain;

import com.demo.DBPBackend.comentario.domain.Comments;
import com.demo.DBPBackend.restaurant.domain.Restaurant;
import com.demo.DBPBackend.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(0)
    @Max(5)
    private Integer calificacion;


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restauranteId")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "valoracion", cascade = CascadeType.ALL)
    private List<Comments> comments;
}
