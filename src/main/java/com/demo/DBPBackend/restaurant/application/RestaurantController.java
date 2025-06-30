package com.demo.DBPBackend.restaurant.application;

import com.demo.DBPBackend.menu.dto.MenuResponseDto;
import com.demo.DBPBackend.restaurant.domain.RestaurantCategory;
import com.demo.DBPBackend.restaurant.domain.RestaurantService;
import com.demo.DBPBackend.restaurant.dto.RestaurantRequestDto;
import com.demo.DBPBackend.restaurant.dto.RestaurantResponseDto;
import com.demo.DBPBackend.restaurant.dto.RestaurantSummaryDto;
import com.demo.DBPBackend.review.dto.ReviewResponseDto;
import com.demo.DBPBackend.comment.dto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<Page<RestaurantSummaryDto>> getAllRestaurants(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(restaurantService.getAllRestaurants(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<RestaurantSummaryDto>> getRestaurantsByCategory(@PathVariable RestaurantCategory category,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(restaurantService.getRestaurantsByCategory(category, page, size));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<RestaurantResponseDto> createRestaurant(@ModelAttribute RestaurantRequestDto restaurantRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.createRestaurant(restaurantRequestDto));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<MenuResponseDto> getRestaurantMenu(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantMenu(id));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<CommentResponseDto>> getRestaurantComments(@PathVariable Long id,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(restaurantService.getRestaurantComments(id, page, size));
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<Page<ReviewResponseDto>> getRestaurantReviews(@PathVariable Long id,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(restaurantService.getRestaurantReviews(id, page, size));
    }

    @GetMapping("/categories")
    public ResponseEntity<RestaurantCategory[]> getRestaurantCategories() {
        return ResponseEntity.ok(RestaurantCategory.values());
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateRestaurant(@PathVariable Long id, @ModelAttribute RestaurantRequestDto restaurantRequestDto) {
        restaurantService.updateRestaurant(id, restaurantRequestDto);
        return ResponseEntity.noContent().build();
    }
}
