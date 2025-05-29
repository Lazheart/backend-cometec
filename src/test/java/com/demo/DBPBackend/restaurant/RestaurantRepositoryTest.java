package com.demo.DBPBackend.restaurant;

import com.demo.DBPBackend.dish.domain.Dish;
import com.demo.DBPBackend.menu.domain.Menu;
import com.demo.DBPBackend.restaurant.domain.Restaurant;
import com.demo.DBPBackend.restaurant.infrastructure.RestaurantRepository;
import com.demo.DBPBackend.location.domain.Location;
import com.demo.DBPBackend.user.domain.Role;
import com.demo.DBPBackend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        // Crear usuario
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

        // Crear restaurante (sin menú todavía)
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Restaurant1");
        restaurant.setOwner(owner);
        restaurant.setLocation(location);

        // Crear menú y asignarlo al restaurante
        Menu menu = new Menu();
        menu.setDishes(new ArrayList<>());
        menu.setRestaurant(restaurant);
        restaurant.setMenu(menu);       // relación bidireccional

        // Persistencia (solo del restaurante si usas cascade = ALL)
        entityManager.persist(restaurant);

        // Crear platos
        Dish dish1 = new Dish();
        dish1.setDescription("First dish");
        dish1.setName("Dish1");
        dish1.setPrice(12.0);
        dish1.setMenu(menu);
        entityManager.persist(dish1);

        Dish dish2 = new Dish();
        dish2.setDescription("Second dish");
        dish2.setName("Dish2");
        dish2.setPrice(24.0);
        dish2.setMenu(menu);
        entityManager.persist(dish2);

        // Agregar platos al menú
        menu.getDishes().add(dish1);
        menu.getDishes().add(dish2);
    }

    @Test
    public void testRestaurantPersistence() {
        var all = restaurantRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getName()).isEqualTo("Restaurant1");
    }

    @Test
    public void testFindByName() {
        var restaurant = restaurantRepository.findByName("Restaurant1");
        assertThat(restaurant).isPresent();
        assertThat(restaurant.get().getName()).isEqualTo("Restaurant1");
    }

    @Test
    public void testRestaurantMenuAndDishes() {
        var restaurant = restaurantRepository.findAll().get(0);

        assertThat(restaurant.getMenu()).isNotNull();
        assertThat(restaurant.getMenu().getDishes()).hasSize(2);
        assertThat(restaurant.getMenu().getDishes())
                .extracting(Dish::getName)
                .containsExactlyInAnyOrder("Dish1", "Dish2");
    }

    @Test
    public void testRestaurantOwner() {
        var restaurant = restaurantRepository.findAll().get(0);
        assertThat(restaurant.getOwner()).isNotNull();
        assertThat(restaurant.getOwner().getName()).isEqualTo("Ana");
    }

    @Test
    public void testRestaurantUbicacion() {
        var restaurant = restaurantRepository.findAll().get(0);
        assertThat(restaurant.getLocation()).isNotNull();
        assertThat(restaurant.getLocation().getLatitud()).isEqualTo(-77.0);
        assertThat(restaurant.getLocation().getLongitud()).isEqualTo(12.0);
    }

    @Test
    public void testDeleteRestaurantCascadesToMenu() {
        var restaurant = restaurantRepository.findAll().get(0);
        restaurantRepository.delete(restaurant);
        entityManager.flush();

        assertThat(restaurantRepository.findAll()).isEmpty();
    }
}