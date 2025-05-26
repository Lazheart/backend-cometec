package com.demo.DBPBackend.comentario.infrastructure;

import com.demo.DBPBackend.comentario.domain.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
}
