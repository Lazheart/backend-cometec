package com.demo.DBPBackend.dish.infrastructure;

import com.demo.DBPBackend.dish.domain.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
}
