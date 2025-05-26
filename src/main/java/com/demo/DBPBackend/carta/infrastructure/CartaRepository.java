package com.demo.DBPBackend.carta.infrastructure;


import com.demo.DBPBackend.carta.domain.Carta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartaRepository extends JpaRepository<Carta, Long> {
}
