package com.demo.DBPBackend.restaurant.infrastructure;

import com.demo.DBPBackend.restaurant.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurant, Long> {
}
