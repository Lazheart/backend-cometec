package com.demo.DBPBackend.location.apllication;

import com.demo.DBPBackend.location.domain.LocationService;
import com.demo.DBPBackend.location.dto.LocationCreateDTO;
import com.demo.DBPBackend.location.dto.LocationDto;
import com.demo.DBPBackend.location.dto.LocationUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<List<LocationDto>> getLocationsByRestaurant( @PathVariable Long id) {
        List<LocationDto> locations = locationService.getLocationsByRestaurant(id);
        return ResponseEntity.ok(locations);
    }

    @PostMapping("/restaurants/{restaurantId}")
    public ResponseEntity<LocationDto> addLocationToRestaurant( @PathVariable Long id, @RequestBody LocationCreateDTO locationCreateDTO) {
        LocationDto createdLocation = locationService.addLocationToRestaurant(id, locationCreateDTO);
        return new ResponseEntity<>(createdLocation, HttpStatus.CREATED);
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> updateLocation(@PathVariable Long locationId, @RequestBody LocationUpdateDto locationUpdateDTO) {
        LocationDto updatedLocation = locationService.updateLocation(locationId, locationUpdateDTO);
        return ResponseEntity.ok(updatedLocation);
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long locationId) {
        locationService.deleteLocation(locationId);
        return ResponseEntity.noContent().build();
    }
}

