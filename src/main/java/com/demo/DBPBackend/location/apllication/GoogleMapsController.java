package com.demo.DBPBackend.location.apllication;

import com.demo.DBPBackend.conf.GoogleMapsConfig;
import com.demo.DBPBackend.location.domain.GoogleMapsLocationService;
import com.demo.DBPBackend.location.dto.GoogleMapsLocationDto;
import com.demo.DBPBackend.location.dto.LocationPageResponseDto;
import com.demo.DBPBackend.location.dto.LocationSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/maps")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Configurar según tus necesidades de CORS
public class GoogleMapsController {

    private final GoogleMapsLocationService googleMapsLocationService;
    private final GoogleMapsConfig googleMapsConfig;

    /**
     * Obtiene todas las ubicaciones para mostrar en Google Maps
     */
    @GetMapping("/locations")
    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    public ResponseEntity<List<GoogleMapsLocationDto>> getAllLocationsForMap() {
        List<GoogleMapsLocationDto> locations = googleMapsLocationService.getAllLocationsForMap();
        return ResponseEntity.ok(locations);
    }

    /**
     * Obtiene ubicaciones dentro de un área geográfica específica con paginación
     */
    @PostMapping("/locations/area")
    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    public ResponseEntity<LocationPageResponseDto> getLocationsInArea(@RequestBody LocationSearchDto searchDto) {
        try {
            LocationPageResponseDto response = googleMapsLocationService.getLocationsInArea(searchDto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene ubicaciones por categoría de restaurante
     */
    @GetMapping("/locations/category/{category}")
    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    public ResponseEntity<List<GoogleMapsLocationDto>> getLocationsByCategory(@PathVariable String category) {
        List<GoogleMapsLocationDto> locations = googleMapsLocationService.getLocationsByCategory(category);
        return ResponseEntity.ok(locations);
    }

    /**
     * Obtiene ubicaciones con filtros combinados
     */
    @PostMapping("/locations/filter")
    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    public ResponseEntity<List<GoogleMapsLocationDto>> getLocationsWithFilters(@RequestBody LocationSearchDto searchDto) {
        List<GoogleMapsLocationDto> locations = googleMapsLocationService.getLocationsWithFilters(searchDto);
        return ResponseEntity.ok(locations);
    }

    /**
     * Endpoint para obtener ubicaciones con parámetros de query (más simple para el frontend)
     */
    @GetMapping("/locations/search")
    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    public ResponseEntity<List<GoogleMapsLocationDto>> searchLocations(
            @RequestParam(required = false) Double minLat,
            @RequestParam(required = false) Double maxLat,
            @RequestParam(required = false) Double minLng,
            @RequestParam(required = false) Double maxLng,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minRating,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        
        LocationSearchDto searchDto = new LocationSearchDto();
        searchDto.setMinLatitud(minLat);
        searchDto.setMaxLatitud(maxLat);
        searchDto.setMinLongitud(minLng);
        searchDto.setMaxLongitud(maxLng);
        searchDto.setCategory(category);
        searchDto.setMinRating(minRating);
        searchDto.setPage(page);
        searchDto.setSize(size);

        List<GoogleMapsLocationDto> locations = googleMapsLocationService.getLocationsWithFilters(searchDto);
        return ResponseEntity.ok(locations);
    }

    /**
     * Obtiene la configuración de Google Maps para el frontend
     */
    @GetMapping("/config")
    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getGoogleMapsConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("enabled", googleMapsConfig.isEnabled());
        config.put("defaultZoom", googleMapsConfig.getDefaultZoom());
        config.put("defaultLat", googleMapsConfig.getDefaultLat());
        config.put("defaultLng", googleMapsConfig.getDefaultLng());
        // No exponer la API key por seguridad
        config.put("apiKeyConfigured", !googleMapsConfig.getApiKey().isEmpty());
        
        return ResponseEntity.ok(config);
    }
} 