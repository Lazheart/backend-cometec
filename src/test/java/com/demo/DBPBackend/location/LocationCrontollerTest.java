package com.demo.DBPBackend.location;

import com.demo.DBPBackend.location.apllication.LocationController;
import com.demo.DBPBackend.location.domain.LocationService;
import com.demo.DBPBackend.location.dto.LocationCreateDTO;
import com.demo.DBPBackend.location.dto.LocationDto;
import com.demo.DBPBackend.location.dto.LocationUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocationCrontollerTest {
    @Mock
    private LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    private LocationDto locationDto;
    private LocationCreateDTO locationCreateDTO;
    private LocationUpdateDto locationUpdateDto;

    @BeforeEach
    void setUp() {
        locationDto = new LocationDto();
        locationDto.setLatitud(40.0);
        locationDto.setLongitud(-3.0);

        locationCreateDTO = new LocationCreateDTO();
        locationCreateDTO.setLatitud(40.0);
        locationCreateDTO.setLongitud(-3.0);

        locationUpdateDto = new LocationUpdateDto();
        locationUpdateDto.setLatitud(41.0);
        locationUpdateDto.setLongitud(-2.0);
    }

    @Test
    void testGetLocationsByRestaurant() {
        when(locationService.getLocationsByRestaurant(1L)).thenReturn(List.of(locationDto));

        ResponseEntity<List<LocationDto>> response = locationController.getLocationsByRestaurant(1L);

        verify(locationService).getLocationsByRestaurant(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(40.0, response.getBody().get(0).getLatitud());
    }

    @Test
    void testAddLocationToRestaurant() {
        when(locationService.addLocationToRestaurant(1L, locationCreateDTO)).thenReturn(locationDto);

        ResponseEntity<LocationDto> response = locationController.addLocationToRestaurant(1L, locationCreateDTO);

        verify(locationService).addLocationToRestaurant(1L, locationCreateDTO);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(40.0, response.getBody().getLatitud());
    }

    @Test
    void testUpdateLocation() {
        when(locationService.updateLocation(1L, locationUpdateDto)).thenReturn(locationDto);

        ResponseEntity<LocationDto> response = locationController.updateLocation(1L, locationUpdateDto);

        verify(locationService).updateLocation(1L, locationUpdateDto);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(40.0, response.getBody().getLatitud());
    }

    @Test
    void testDeleteLocation() {
        ResponseEntity<Void> response = locationController.deleteLocation(1L);

        verify(locationService).deleteLocation(1L);
        assertEquals(204, response.getStatusCodeValue());
    }
}

