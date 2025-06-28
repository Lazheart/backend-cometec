package com.demo.DBPBackend.menu.infrastructure;

import com.demo.DBPBackend.menu.domain.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long>
{
    Optional<Menu> findByRestaurantId(Long restaurantId);
    List<Menu> findAll();

    Page<Menu> findAll(Pageable pageable);
}
