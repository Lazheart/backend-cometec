package com.demo.DBPBackend.location;

import com.demo.DBPBackend.location.domain.GoogleMapsLocationService;
import com.demo.DBPBackend.location.domain.Location;
import com.demo.DBPBackend.location.dto.GoogleMapsLocationDto;
import com.demo.DBPBackend.location.dto.LocationSearchDto;
import com.demo.DBPBackend.location.infrastructure.LocationRepository;
import com.demo.DBPBackend.restaurant.domain.Restaurant;
import com.demo.DBPBackend.restaurant.domain.RestaurantCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoogleMapsLocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private GoogleMapsLocationService googleMapsLocationService;

    private Location location1;
    private Location location2;
    private Restaurant restaurant1;
    private Restaurant restaurant2;

    @BeforeEach
    void setUp() {
        // Crear restaurantes de prueba
        restaurant1 = new Restaurant();
        restaurant1.setId(1L);
        restaurant1.setName("Restaurante Italiano");
        restaurant1.setAddress("Calle Italia 123");
        restaurant1.setCategory(RestaurantCategory.ITALIAN);
        restaurant1.setImageUrl("https://example.com/italian.jpg");

        restaurant2 = new Restaurant();
        restaurant2.setId(2L);
        restaurant2.setName("Restaurante Español");
        restaurant2.setAddress("Calle España 456");
        restaurant2.setCategory(RestaurantCategory.SPANISH);
        restaurant2.setImageUrl("https://example.com/spanish.jpg");

        // Crear ubicaciones de prueba
        location1 = new Location();
        location1.setId(1L);
        location1.setLatitud(40.4168);
        location1.setLongitud(-3.7038);
        location1.setRestaurant(restaurant1);

        location2 = new Location();
        location2.setId(2L);
        location2.setLatitud(40.4170);
        location2.setLongitud(-3.7040);
        location2.setRestaurant(restaurant2);
    }

    @Test
    void getAllLocationsForMap_ShouldReturnAllLocations() {
        // Arrange
        List<Location> locations = Arrays.asList(location1, location2);
        Page<Location> locationPage = new PageImpl<>(locations);
        Pageable pageable = PageRequest.of(0, 100);

        when(locationRepository.findAllWithRestaurantInfo(pageable)).thenReturn(locationPage);

        // Act
        List<GoogleMapsLocationDto> result = googleMapsLocationService.getAllLocationsForMap();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        
        GoogleMapsLocationDto firstLocation = result.get(0);
        assertEquals(1L, firstLocation.getId());
        assertEquals(40.4168, firstLocation.getLatitud());
        assertEquals(-3.7038, firstLocation.getLongitud());
        assertEquals("Restaurante Italiano", firstLocation.getRestaurantName());
        assertEquals("ITALIAN", firstLocation.getRestaurantCategory());
    }

    @Test
    void getLocationsInArea_WithValidParameters_ShouldReturnLocations() {
        // Arrange
        LocationSearchDto searchDto = new LocationSearchDto();
        searchDto.setMinLatitud(40.4000);
        searchDto.setMaxLatitud(40.4300);
        searchDto.setMinLongitud(-3.7200);
        searchDto.setMaxLongitud(-3.6900);
        searchDto.setPage(0);
        searchDto.setSize(20);

        List<Location> locations = Arrays.asList(location1, location2);
        Page<Location> locationPage = new PageImpl<>(locations);

        when(locationRepository.findLocationsInArea(
                searchDto.getMinLatitud(),
                searchDto.getMaxLatitud(),
                searchDto.getMinLongitud(),
                searchDto.getMaxLongitud(),
                any(Pageable.class)
        )).thenReturn(locationPage);

        // Act
        var result = googleMapsLocationService.getLocationsInArea(searchDto);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getLocations().size());
        assertEquals(0, result.getCurrentPage());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void getLocationsInArea_WithInvalidParameters_ShouldThrowException() {
        // Arrange
        LocationSearchDto searchDto = new LocationSearchDto();
        // No establecer parámetros de área

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            googleMapsLocationService.getLocationsInArea(searchDto);
        });
    }

    @Test
    void getLocationsByCategory_ShouldReturnFilteredLocations() {
        // Arrange
        String category = "ITALIAN";
        List<Location> locations = Arrays.asList(location1);
        Page<Location> locationPage = new PageImpl<>(locations);
        Pageable pageable = PageRequest.of(0, 50);

        when(locationRepository.findByRestaurantCategory(category, pageable)).thenReturn(locationPage);

        // Act
        List<GoogleMapsLocationDto> result = googleMapsLocationService.getLocationsByCategory(category);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ITALIAN", result.get(0).getRestaurantCategory());
    }

    @Test
    void getLocationsWithFilters_WithAreaAndCategory_ShouldReturnFilteredLocations() {
        // Arrange
        LocationSearchDto searchDto = new LocationSearchDto();
        searchDto.setMinLatitud(40.4000);
        searchDto.setMaxLatitud(40.4300);
        searchDto.setMinLongitud(-3.7200);
        searchDto.setMaxLongitud(-3.6900);
        searchDto.setCategory("ITALIAN");
        searchDto.setPage(0);
        searchDto.setSize(20);

        List<Location> locations = Arrays.asList(location1, location2);
        Page<Location> locationPage = new PageImpl<>(locations);

        when(locationRepository.findLocationsInArea(
                searchDto.getMinLatitud(),
                searchDto.getMaxLatitud(),
                searchDto.getMinLongitud(),
                searchDto.getMaxLongitud(),
                any(Pageable.class)
        )).thenReturn(locationPage);

        // Act
        List<GoogleMapsLocationDto> result = googleMapsLocationService.getLocationsWithFilters(searchDto);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size()); // Solo debe devolver el restaurante italiano
        assertEquals("ITALIAN", result.get(0).getRestaurantCategory());
    }
} 