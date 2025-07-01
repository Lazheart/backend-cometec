package com.demo.DBPBackend.location.infrastructure;

import com.demo.DBPBackend.location.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByRestaurantId(Long restaurantId);

}
