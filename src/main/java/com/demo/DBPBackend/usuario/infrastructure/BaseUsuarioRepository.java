package com.demo.DBPBackend.usuario.infrastructure;

import com.demo.DBPBackend.usuario.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseUsuarioRepository<T extends Usuario> extends JpaRepository<T, Long> {
}
