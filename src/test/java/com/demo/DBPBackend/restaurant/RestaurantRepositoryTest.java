package com.demo.DBPBackend.restaurant;

import com.demo.DBPBackend.dish.domain.Dish;
import com.demo.DBPBackend.menu.domain.Menu;
import com.demo.DBPBackend.restaurant.domain.Restaurant;
import com.demo.DBPBackend.restaurant.infrastructure.RestaurantRepository;
import com.demo.DBPBackend.ubicacion.domain.Ubicacion;
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

        //Creacion de usuario
        User owner = new User();
        owner.setName("Ana");
        owner.setLastname("Pérez");
        owner.setEmail("ana.perez@example.com");
        owner.setPassword("securePass123");
        owner.setPhone("999888777");
        owner.setRole(Role.OWNER); // Asumiendo que tienes este enum
        owner.setCreatedAt(LocalDateTime.now());
        entityManager.persist(owner);

        //Creación de la ubicación
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLongitud(12.0);
        ubicacion.setLatitud(-77.0);
        entityManager.persist(ubicacion);

        //Creación de platos
        Dish dish1 = new Dish();
        dish1.setDescription("Frist dish");
        dish1.setName("Dish1");
        dish1.setPrice(12.0);
        entityManager.persist(dish1);

        Dish dish2 = new Dish();
        dish2.setDescription("Second dish");
        dish2.setName("Dish2");
        dish2.setPrice(24.0);
        entityManager.persist(dish2);

        //Creación del menú
        Menu menu = new Menu();
        menu.setDishes(new ArrayList<>());
        menu.getDishes().add(dish1);
        menu.getDishes().add(dish2);
        entityManager.persist(menu);

        //Creacion del restaurante
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Restaurant1");
        restaurant.setOwner(owner);
        restaurant.setUbicacion(ubicacion);
        restaurant.setMenu(menu);
        entityManager.persist(restaurant);
    }

    @Test
    public void testRestaurantPersistence() {
        var all = restaurantRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getName()).isEqualTo("Restaurant1");
    }


}