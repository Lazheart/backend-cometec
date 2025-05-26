package com.demo.DBPBackend.valoracion.domain;

import com.demo.DBPBackend.comentario.domain.Comentario;
import com.demo.DBPBackend.restaurante.domain.Restaurante;
import com.demo.DBPBackend.usuario.domain.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Valoracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(0)
    @Max(5)
    @NotBlank
    private Integer calificacion;

    @CurrentTimestamp
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "usuarioId")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "restauranteId")
    private Restaurante restaurante;

    @OneToMany(mappedBy = "valoracion", cascade = CascadeType.ALL)
    private List<Comentario> comentarios;
}
