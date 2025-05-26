package com.demo.DBPBackend.plato.infrastructure;

import com.demo.DBPBackend.plato.domain.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatoRepository extends JpaRepository<Plato, Long> {
}
