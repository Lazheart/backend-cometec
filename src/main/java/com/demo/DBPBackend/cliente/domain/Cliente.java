package com.demo.DBPBackend.cliente.domain;

import com.demo.DBPBackend.comentario.domain.Comentario;
import com.demo.DBPBackend.ubicacion.domain.Ubicacion;
import com.demo.DBPBackend.usuario.domain.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Cliente extends Usuario {

    @ManyToOne
    @JoinColumn(name = "ubicacionActualID")
    private Ubicacion ubicacionActual;

    @OneToMany(mappedBy = "cliente")
    private List<Comentario> comentarios;
}
