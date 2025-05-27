package com.demo.DBPBackend.comentario.domain;

import com.demo.DBPBackend.cliente.domain.Cliente;
import com.demo.DBPBackend.user.domain.User;
import com.demo.DBPBackend.review.domain.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(length = 550)
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "valoracionId")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User cliente;
}
