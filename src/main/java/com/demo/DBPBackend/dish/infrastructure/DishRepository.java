package com.demo.DBPBackend.dish.infrastructure;

import com.demo.DBPBackend.dish.domain.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByMenuId(Long menuId);
}
