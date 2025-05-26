package com.demo.DBPBackend.valoracion.infrastructure;

import com.demo.DBPBackend.valoracion.domain.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValoracionRepository extends JpaRepository<Valoracion, Long> {
}
