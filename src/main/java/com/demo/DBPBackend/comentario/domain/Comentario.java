package com.demo.DBPBackend.comentario.domain;

import com.demo.DBPBackend.cliente.domain.Cliente;
import com.demo.DBPBackend.valoracion.domain.Valoracion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(length = 550)
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "valoracionId")
    private Valoracion valoracion;

    @ManyToOne
    @JoinColumn(name = "clienteId")
    private Cliente cliente;
}
