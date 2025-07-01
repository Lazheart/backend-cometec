package com.demo.DBPBackend.location.domain;

import com.demo.DBPBackend.exceptions.ResourceAlreadyExistException;
import com.demo.DBPBackend.location.dto.LocationCreateDTO;
import com.demo.DBPBackend.location.dto.LocationDto;
import com.demo.DBPBackend.location.dto.LocationUpdateDto;
import com.demo.DBPBackend.location.infrastructure.LocationRepository;
import com.demo.DBPBackend.restaurant.domain.Restaurant;
import com.demo.DBPBackend.exceptions.ResourceNotFoundException;
import com.demo.DBPBackend.restaurant.infrastructure.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    public LocationDto getLocationByRestaurant(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurante no encontrado con ID: " + restaurantId);
        }

        Location location = locationRepository.findByRestaurantId(restaurantId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró ubicación para el restaurante con ID: " + restaurantId));
        
        return modelMapper.map(location, LocationDto.class);
    }

    @Transactional
    public LocationDto addLocationToRestaurant(Long restaurantId, LocationCreateDTO locationCreateDTO) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante no encontrado con ID: " + restaurantId));

        if (restaurant.getLocation() != null) {
            throw new ResourceAlreadyExistException("El restaurante ya tiene una ubicación asignada");
        }

        Location location = modelMapper.map(locationCreateDTO, Location.class);
        Location savedLocation = locationRepository.save(location);

        // Asignar la ubicación al restaurante
        restaurant.setLocation(savedLocation);
        restaurantRepository.save(restaurant);

        return modelMapper.map(savedLocation, LocationDto.class);
    }

    @Transactional
    public LocationDto updateLocation(Long locationId, LocationUpdateDto LocationUpdateDto) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Ubicación no encontrada con ID: " + locationId));

        if (LocationUpdateDto.getLatitud() != null) {
            location.setLatitud(LocationUpdateDto.getLatitud());
        }

        if (LocationUpdateDto.getLongitud() != null) {
            location.setLongitud(LocationUpdateDto.getLongitud());
        }

        Location updatedLocation = locationRepository.save(location);
        return modelMapper.map(updatedLocation, LocationDto.class);
    }

    @Transactional
    public void deleteLocation(Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Ubicación no encontrada con ID: " + locationId));
        locationRepository.delete(location);
    }

    public List<LocationDto> getNearbyLocations(double latitud, double longitud, double radioKm) {
        List<Location> allLocations = locationRepository.findAll();
        return allLocations.stream()
                .filter(loc -> haversine(latitud, longitud, loc.getLatitud(), loc.getLongitud()) <= radioKm)
                .map(loc -> {
                    LocationDto dto = new LocationDto();
                    dto.setId(loc.getId());
                    dto.setLatitud(loc.getLatitud());
                    dto.setLongitud(loc.getLongitud());
                    if (loc.getRestaurant() != null) {
                        dto.setRestaurantId(loc.getRestaurant().getId());
                        dto.setRestaurantName(loc.getRestaurant().getName());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radio de la tierra en km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
