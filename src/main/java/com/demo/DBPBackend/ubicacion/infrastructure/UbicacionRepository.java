package com.demo.DBPBackend.ubicacion.infrastructure;

import com.demo.DBPBackend.ubicacion.domain.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
}
