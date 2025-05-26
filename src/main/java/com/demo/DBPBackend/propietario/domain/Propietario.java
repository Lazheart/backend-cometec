package com.demo.DBPBackend.propietario.domain;

import com.demo.DBPBackend.restaurante.domain.Restaurante;
import com.demo.DBPBackend.usuario.domain.Usuario;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Propietario extends Usuario {

    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    @Column(length = 15, unique = true, nullable = false)
    private String numeroDocumento;

    @OneToMany(mappedBy = "propietario")
    private List<Restaurante> restaurantes;

}
