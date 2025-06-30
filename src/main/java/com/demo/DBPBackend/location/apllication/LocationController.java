package com.demo.DBPBackend.location.apllication;

import com.demo.DBPBackend.location.domain.LocationService;
import com.demo.DBPBackend.location.dto.LocationCreateDTO;
import com.demo.DBPBackend.location.dto.LocationDto;
import com.demo.DBPBackend.location.dto.LocationUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;


    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<LocationDto> getLocationByRestaurant( @PathVariable Long restaurantId) {
        LocationDto location = locationService.getLocationByRestaurant(restaurantId);
        return ResponseEntity.ok(location);
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @PostMapping("/restaurants/{restaurantId}")
    public ResponseEntity<LocationDto> addLocationToRestaurant( @PathVariable Long restaurantId, @RequestBody LocationCreateDTO locationCreateDTO) {
        LocationDto createdLocation = locationService.addLocationToRestaurant(restaurantId, locationCreateDTO);
        return new ResponseEntity<>(createdLocation, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> updateLocation(@PathVariable Long locationId, @RequestBody LocationUpdateDto locationUpdateDTO) {
        LocationDto updatedLocation = locationService.updateLocation(locationId, locationUpdateDTO);
        return ResponseEntity.ok(updatedLocation);
    }


    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @DeleteMapping("/{locationId}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long locationId) {
        locationService.deleteLocation(locationId);
        return ResponseEntity.noContent().build();
    }
}

