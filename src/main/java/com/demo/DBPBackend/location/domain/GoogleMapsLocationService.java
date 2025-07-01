package com.demo.DBPBackend.location.domain;

import com.demo.DBPBackend.location.dto.GoogleMapsLocationDto;
import com.demo.DBPBackend.location.dto.LocationPageResponseDto;
import com.demo.DBPBackend.location.dto.LocationSearchDto;
import com.demo.DBPBackend.location.infrastructure.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoogleMapsLocationService {

    private final LocationRepository locationRepository;

    /**
     * Obtiene todas las ubicaciones con información del restaurante para Google Maps
     */
    public List<GoogleMapsLocationDto> getAllLocationsForMap() {
        Pageable pageable = PageRequest.of(0, 100); // Límite inicial de 100 ubicaciones
        Page<Location> locations = locationRepository.findAllWithRestaurantInfo(pageable);
        
        return locations.getContent().stream()
                .map(this::mapToGoogleMapsDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene ubicaciones dentro de un área geográfica específica con paginación
     */
    public LocationPageResponseDto getLocationsInArea(LocationSearchDto searchDto) {
        if (searchDto.getMinLatitud() == null || searchDto.getMaxLatitud() == null ||
            searchDto.getMinLongitud() == null || searchDto.getMaxLongitud() == null) {
            throw new IllegalArgumentException("Todos los parámetros de área geográfica son requeridos");
        }

        Pageable pageable = PageRequest.of(searchDto.getPage(), searchDto.getSize());
        Page<Location> locations = locationRepository.findLocationsInArea(
                searchDto.getMinLatitud(),
                searchDto.getMaxLatitud(),
                searchDto.getMinLongitud(),
                searchDto.getMaxLongitud(),
                pageable
        );

        List<GoogleMapsLocationDto> locationDtos = locations.getContent().stream()
                .map(this::mapToGoogleMapsDto)
                .collect(Collectors.toList());

        return new LocationPageResponseDto(
                locationDtos,
                locations.getNumber(),
                locations.getTotalPages(),
                locations.getTotalElements(),
                locations.hasNext(),
                locations.hasPrevious()
        );
    }

    /**
     * Obtiene ubicaciones por categoría de restaurante
     */
    public List<GoogleMapsLocationDto> getLocationsByCategory(String category) {
        Pageable pageable = PageRequest.of(0, 50);
        Page<Location> locations = locationRepository.findByRestaurantCategory(category, pageable);
        
        return locations.getContent().stream()
                .map(this::mapToGoogleMapsDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene ubicaciones con filtros combinados
     */
    public List<GoogleMapsLocationDto> getLocationsWithFilters(LocationSearchDto searchDto) {
        List<GoogleMapsLocationDto> locations;
        
        // Si se especifica un área geográfica, buscar en esa área
        if (searchDto.getMinLatitud() != null && searchDto.getMaxLatitud() != null &&
            searchDto.getMinLongitud() != null && searchDto.getMaxLongitud() != null) {
            locations = getLocationsInArea(searchDto);
        } else {
            // Si no se especifica área, obtener todas las ubicaciones
            locations = getAllLocationsForMap();
        }

        // Aplicar filtros adicionales
        if (searchDto.getCategory() != null && !searchDto.getCategory().isEmpty()) {
            locations = locations.stream()
                    .filter(location -> searchDto.getCategory().equals(location.getRestaurantCategory()))
                    .collect(Collectors.toList());
        }

        if (searchDto.getMinRating() != null) {
            locations = locations.stream()
                    .filter(location -> location.getAverageRating() != null && 
                            location.getAverageRating() >= searchDto.getMinRating())
                    .collect(Collectors.toList());
        }

        return locations;
    }

    /**
     * Mapea una entidad Location a GoogleMapsLocationDto
     */
    private GoogleMapsLocationDto mapToGoogleMapsDto(Location location) {
        String restaurantName = location.getRestaurant() != null ? location.getRestaurant().getName() : "Sin nombre";
        String restaurantAddress = location.getRestaurant() != null ? location.getRestaurant().getAddress() : "";
        String restaurantCategory = location.getRestaurant() != null && location.getRestaurant().getCategory() != null ? 
                location.getRestaurant().getCategory().name() : "";
        String imageUrl = location.getRestaurant() != null ? location.getRestaurant().getImageUrl() : "";
        
        // Aquí podrías calcular el rating promedio si tienes reviews
        Double averageRating = 0.0; // Placeholder - implementar lógica de reviews si es necesario
        
        return new GoogleMapsLocationDto(
                location.getId(),
                location.getLatitud(),
                location.getLongitud(),
                restaurantName,
                restaurantAddress,
                restaurantCategory,
                averageRating,
                imageUrl
        );
    }
} 