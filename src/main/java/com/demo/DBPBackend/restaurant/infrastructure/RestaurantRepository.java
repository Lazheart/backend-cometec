package com.demo.DBPBackend.restaurant.infrastructure;

import com.demo.DBPBackend.restaurant.domain.Restaurant;
import com.demo.DBPBackend.restaurant.domain.RestaurantCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // Métodos originales sin paginación (mantener para compatibilidad)
    List<Restaurant> findAll();
    Optional<Restaurant> findByName(String name);
    
    // Métodos de paginación básicos
    Page<Restaurant> findAll(Pageable pageable);
    
    // Métodos de búsqueda con paginación
    Page<Restaurant> findByNameContaining(String name, Pageable pageable);
    Page<Restaurant> findByOwnerId(Long ownerId, Pageable pageable);
    Page<Restaurant> findByCategory(RestaurantCategory category, Pageable pageable);
    Page<Restaurant> findByFavouritedBy_Id(Long userId, Pageable pageable);
    Page<Restaurant> findByNameContainingAndCategory(String name, RestaurantCategory category, Pageable pageable);

    // Métodos con ordenamiento
    Page<Restaurant> findAllByOrderByNameAsc(Pageable pageable);
    Page<Restaurant> findAllByOrderByIdDesc(Pageable pageable);
    
    // Métodos de búsqueda por relaciones
    Page<Restaurant> findByOwnerIdOrderByIdDesc(Long ownerId, Pageable pageable);
    
    // Métodos de búsqueda por categoría con ordenamiento
    Page<Restaurant> findByCategoryOrderByNameAsc(RestaurantCategory category, Pageable pageable);
    Page<Restaurant> findByCategoryOrderByIdDesc(RestaurantCategory category, Pageable pageable);
}
