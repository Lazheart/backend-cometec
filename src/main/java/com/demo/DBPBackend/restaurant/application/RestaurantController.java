package com.demo.DBPBackend.restaurant.application;


import com.demo.DBPBackend.menu.dto.MenuResponseDto;
import com.demo.DBPBackend.restaurant.domain.RestaurantService;
import com.demo.DBPBackend.restaurant.dto.RestaurantRequestDto;
import com.demo.DBPBackend.restaurant.dto.RestaurantResponseDto;
import com.demo.DBPBackend.restaurant.dto.RestaurantSummaryDto;
import com.demo.DBPBackend.review.dto.ReviewResponseDto;
import com.demo.DBPBackend.comment.dto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // Listar todos los restaurantes
    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<RestaurantSummaryDto>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    // Obtener detalles de un restaurante
    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponseDto> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }

    // Obtener reseñas del restaurante
    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @GetMapping("/{restaurantId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getRestaurantReviews(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantReviews(id));
    }

    // Obtener comentarios del restaurante
    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @GetMapping("/{restaurantId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getRestaurantComments(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantComments(id));
    }

    // Obtener carta del restaurante
    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    @GetMapping("/{restaurantId}/menu")
    public ResponseEntity<MenuResponseDto> getRestaurantMenu(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantMenu(id));
    }

    // Crear restaurante (solo propietarios)
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping
    public ResponseEntity<Void> createRestaurant(@RequestBody RestaurantRequestDto dto) {
        restaurantService.createRestaurant(dto);
        return ResponseEntity.status(201).build();
    }

    // Actualizar restaurante (propietario)
    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/{restaurantId}")
    public ResponseEntity<Void> updateRestaurant(@PathVariable Long id, @RequestBody RestaurantRequestDto dto) {
        restaurantService.updateRestaurant(id, dto);
        return ResponseEntity.ok().build();
    }

    // Eliminar restaurante
    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}
