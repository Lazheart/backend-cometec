package com.demo.DBPBackend.cliente.domain;

import com.demo.DBPBackend.comentario.domain.Comentario;
import com.demo.DBPBackend.ubicacion.domain.Ubicacion;
import com.demo.DBPBackend.usuario.domain.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Cliente extends Usuario {

    @ManyToOne
    @JoinColumn(name = "ubicacionActualID")
    private Ubicacion ubicacionActual;

    @OneToMany(mappedBy = "cliente")
    private List<Comentario> comentarios;
}
