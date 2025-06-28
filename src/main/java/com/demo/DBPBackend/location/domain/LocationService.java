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
import org.springframework.transaction.annotation.Transactional;

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

    public LocationDto getLocationById(Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Ubicación no encontrada con ID: " + locationId));

        return modelMapper.map(location, LocationDto.class);
    }
}
