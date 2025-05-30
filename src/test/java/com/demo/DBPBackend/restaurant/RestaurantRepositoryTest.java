package com.demo.DBPBackend.restaurant;

import com.demo.DBPBackend.location.domain.Location;
import com.demo.DBPBackend.menu.domain.Menu;
import com.demo.DBPBackend.restaurant.domain.Restaurant;
import com.demo.DBPBackend.restaurant.infrastructure.RestaurantRepository;
import com.demo.DBPBackend.user.domain.Role;
import com.demo.DBPBackend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
public class RestaurantRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Restaurant restaurant;

    @BeforeEach
    public void setUp() {
        // Crear usuario propietario
        User owner = new User();
        owner.setName("Ana");
        owner.setLastname("Pérez");
        owner.setEmail("ana.perez@example.com");
        owner.setPassword("securePass123");
        owner.setPhone("999888777");
        owner.setRole(Role.OWNER);
        owner.setCreatedAt(LocalDateTime.now());
        entityManager.persist(owner);

        // Crear ubicación
        Location location = new Location();
        location.setLongitud(12.0);
        location.setLatitud(-77.0);
        entityManager.persist(location);

        // Crear restaurante
        restaurant = new Restaurant();
        restaurant.setName("Restaurant1");
        restaurant.setOwner(owner);
        restaurant.setLocation(location);

        // Crear menú y establecer la relación bidireccional
        Menu menu = new Menu();
        menu.setRestaurant(restaurant);  // Establece la relación inversa
        restaurant.setMenu(menu);        // Establece la relación principal

        // Persistir primero el restaurante
        entityManager.persist(restaurant);
        // Luego el menú (esto debería funcionar gracias a @Transactional)
        entityManager.persist(menu);
    }

    @Test
    public void testSaveAndFindRestaurant() {
        // Verificar que el restaurante se guardó correctamente
        Optional<Restaurant> foundRestaurant = restaurantRepository.findById(restaurant.getId());
        assertTrue(foundRestaurant.isPresent());
        assertEquals("Restaurant1", foundRestaurant.get().getName());

        // Verificar la relación con el menú
        assertNotNull(foundRestaurant.get().getMenu());
        assertEquals(restaurant.getId(), foundRestaurant.get().getMenu().getRestaurant().getId());
    }

    @Test
    public void testFindAll() {
        List<Restaurant> allRestaurants = restaurantRepository.findAll();
        assertThat(allRestaurants).hasSize(1);
        assertThat(allRestaurants.get(0).getName()).isEqualTo("Restaurant1");
    }

    @Test
    public void testFindByName() {
        Optional<Restaurant> foundRestaurant = restaurantRepository.findByName("Restaurant1");
        assertTrue(foundRestaurant.isPresent());
        assertEquals(restaurant.getId(), foundRestaurant.get().getId());
    }

    @Test
    public void testUpdateRestaurant() {
        restaurant.setName("Updated Restaurant");
        restaurantRepository.save(restaurant);

        Optional<Restaurant> updatedRestaurant = restaurantRepository.findById(restaurant.getId());
        assertTrue(updatedRestaurant.isPresent());
        assertEquals("Updated Restaurant", updatedRestaurant.get().getName());
    }

    @Test
    public void testDeleteRestaurant() {
        restaurantRepository.delete(restaurant);

        Optional<Restaurant> deletedRestaurant = restaurantRepository.findById(restaurant.getId());
        assertFalse(deletedRestaurant.isPresent());
    }
}