package com.demo.DBPBackend.restaurante.infrastructure;

import com.demo.DBPBackend.restaurante.domain.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
}
