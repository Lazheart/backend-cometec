package com.demo.DBPBackend.propietario.domain;

import com.demo.DBPBackend.restaurante.domain.Restaurante;
import com.demo.DBPBackend.usuario.domain.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
public class Propietario extends Usuario {

    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    @Column(length = 15, unique = true, nullable = false)
    private Integer numeroDocumento;

    @OneToMany(mappedBy = "propietario")
    private List<Restaurante> restaurantes;

}
