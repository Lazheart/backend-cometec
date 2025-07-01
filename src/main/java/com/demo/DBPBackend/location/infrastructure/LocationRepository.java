package com.demo.DBPBackend.location.infrastructure;

import com.demo.DBPBackend.location.domain.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByRestaurantId(Long restaurantId);
    
    // Buscar ubicaciones dentro de un área geográfica
    @Query("SELECT l FROM Location l WHERE l.latitud BETWEEN :minLat AND :maxLat AND l.longitud BETWEEN :minLng AND :maxLng")
    Page<Location> findLocationsInArea(
        @Param("minLat") Double minLat, 
        @Param("maxLat") Double maxLat, 
        @Param("minLng") Double minLng, 
        @Param("maxLng") Double maxLng, 
        Pageable pageable
    );
    
    // Buscar todas las ubicaciones con información del restaurante
    @Query("SELECT l FROM Location l JOIN l.restaurant r")
    Page<Location> findAllWithRestaurantInfo(Pageable pageable);
    
    // Buscar ubicaciones por categoría de restaurante
    @Query("SELECT l FROM Location l JOIN l.restaurant r WHERE r.category = :category")
    Page<Location> findByRestaurantCategory(@Param("category") String category, Pageable pageable);
}
