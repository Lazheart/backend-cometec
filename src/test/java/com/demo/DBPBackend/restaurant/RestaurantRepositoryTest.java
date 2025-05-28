package com.demo.DBPBackend.restaurant;

import com.demo.DBPBackend.auth.utils.AuthUtils;
import com.demo.DBPBackend.dish.domain.Dish;
import com.demo.DBPBackend.menu.domain.Menu;
import com.demo.DBPBackend.restaurant.domain.Restaurant;
import com.demo.DBPBackend.restaurant.domain.RestaurantService;
import com.demo.DBPBackend.restaurant.dto.RestaurantRequestDto;
import com.demo.DBPBackend.restaurant.infrastructure.RestaurantRepository;
import com.demo.DBPBackend.ubicacion.domain.Ubicacion;
import com.demo.DBPBackend.ubicacion.dto.UbicacionDto;
import com.demo.DBPBackend.ubicacion.infrastructure.UbicacionRepository;
import com.demo.DBPBackend.user.domain.Role;
import com.demo.DBPBackend.user.domain.User;
import com.demo.DBPBackend.user.infrastructure.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Restaurant restaurant;

    private User owner;

    private Ubicacion ubicacion;

    @BeforeEach
    void setUp() {

        owner = new User();
        owner.setName("John");
        owner.setLastname("Doe");
        owner.setEmail("john.doe@example.com");
        owner.setPassword("securePass123"); // Debe tener entre 8 y 128 caracteres
        owner.setPhone("1234567890");
        owner.setRole(Role.OWNER); // Asegúrate de tener este enum
        owner.setCreatedAt(LocalDateTime.now());

        userRepository.save(owner);

        // Simula que este usuario está autenticado
        //when(authUtils.getCurrentUserEmail()).thenReturn("john.doe@example.com");
        //when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(owner));


        ubicacion = new Ubicacion();
        ubicacion.setLatitud(12.0);
        ubicacion.setLongitud(-77.0);


        restaurant = new Restaurant();
        restaurant.setName("My Restaurant");
        restaurant.setOwner(owner);
        restaurant.setUbicacion(ubicacion);

        //Creamos Menú y lo asociamos al restaurante
        Menu menu = new Menu();
        menu.setRestaurant(restaurant);

        //Asociamos el restaurante con el menú
        restaurant.setMenu(menu);

        //Creamos los platos y los asociamos al menu del restaurante
        Dish dish1 = new Dish();
        dish1.setName("Dish 1");
        dish1.setDescription("Dish 1 Description");
        dish1.setPrice(12.0);
        dish1.setMenu(menu);

        Dish dish2 = new Dish();
        dish2.setName("Dish 2");
        dish2.setDescription("Dish 2 Description");
        dish2.setPrice(24.0);
        dish2.setMenu(menu);

        //Agreagmos los dishes
        menu.getDishes().add(dish1);
        menu.getDishes().add(dish2);

        restaurantRepository.save(restaurant);
    }

    @Test
    void testCreateRestaurant_SavesSuccessfully() {
        // Crear DTO de ubicación
        UbicacionDto ubicacionDto = new UbicacionDto();
        ubicacionDto.setLatitud(12.0);
        ubicacionDto.setLongitud(-77.0);

        // Crear DTO de restaurante
        RestaurantRequestDto restaurantRequestDto = new RestaurantRequestDto();
        restaurantRequestDto.setName("My Restaurant");
        restaurantRequestDto.setUbicacion(ubicacionDto);

        // Buscar restaurante en repo para validar
        Optional<Restaurant> optRestaurant = restaurantRepository.findByName("My Restaurant");
        assertTrue(optRestaurant.isPresent(), "El restaurante debería haberse guardado");

        Restaurant savedRestaurant = optRestaurant.get();

        // Validar datos
        assertEquals("My Restaurant", savedRestaurant.getName());
        assertEquals(owner.getId(), savedRestaurant.getOwner().getId());

        assertNotNull(savedRestaurant.getUbicacion());
        assertEquals(12.0, savedRestaurant.getUbicacion().getLatitud());
        assertEquals(-77.0, savedRestaurant.getUbicacion().getLongitud());
    }




}
