package com.demo.DBPBackend.dish.infrastructure;

import com.demo.DBPBackend.dish.domain.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByMenuId(Long menuId);
    
    Page<Dish> findAll(Pageable pageable);
    
    Page<Dish> findByMenuId(Long menuId, Pageable pageable);
    Page<Dish> findByNameContaining(String name, Pageable pageable);
    Page<Dish> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);
    
    Page<Dish> findAllByOrderByNameAsc(Pageable pageable);
    Page<Dish> findAllByOrderByPriceAsc(Pageable pageable);
    Page<Dish> findAllByOrderByPriceDesc(Pageable pageable);
    
    Page<Dish> findByMenuIdOrderByNameAsc(Long menuId, Pageable pageable);
    Page<Dish> findByMenuIdOrderByPriceAsc(Long menuId, Pageable pageable);
    Page<Dish> findByMenuIdOrderByPriceDesc(Long menuId, Pageable pageable);
}
