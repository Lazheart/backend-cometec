package com.demo.DBPBackend.dish.application;

import com.demo.DBPBackend.dish.domain.DishService;
import com.demo.DBPBackend.dish.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Page<DishResponseDto>> getAllDishes(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dishService.getAllDishes(page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all/name")
    public ResponseEntity<Page<DishResponseDto>> getAllDishesOrderedByName(@RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dishService.getAllDishesOrderedByName(page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all/price-asc")
    public ResponseEntity<Page<DishResponseDto>> getAllDishesOrderedByPriceAsc(@RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dishService.getAllDishesOrderedByPriceAsc(page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all/price-desc")
    public ResponseEntity<Page<DishResponseDto>> getAllDishesOrderedByPriceDesc(@RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dishService.getAllDishesOrderedByPriceDesc(page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<DishResponseDto> getDishById(@PathVariable Long id) {
        return ResponseEntity.ok(dishService.getDishById(id));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/menu/{menuId}")
    public ResponseEntity<Page<DishSummaryDto>> getDishesByMenu(@PathVariable Long menuId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dishService.getDishesByMenuId(menuId, page, size));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/menu/{menuId}/name")
    public ResponseEntity<Page<DishSummaryDto>> getDishesByMenuOrderedByName(@PathVariable Long menuId,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dishService.getDishesByMenuIdOrderedByName(menuId, page, size));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/menu/{menuId}/price-asc")
    public ResponseEntity<Page<DishSummaryDto>> getDishesByMenuOrderedByPriceAsc(@PathVariable Long menuId,
                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dishService.getDishesByMenuIdOrderedByPriceAsc(menuId, page, size));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/menu/{menuId}/price-desc")
    public ResponseEntity<Page<DishSummaryDto>> getDishesByMenuOrderedByPriceDesc(@PathVariable Long menuId,
                                                                                  @RequestParam(defaultValue = "0") int page,
                                                                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dishService.getDishesByMenuIdOrderedByPriceDesc(menuId, page, size));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<Page<DishResponseDto>> getDishesByRestaurant(@PathVariable Long restaurantId,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dishService.getDishesByRestaurantId(restaurantId, page, size));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/name")
    public ResponseEntity<Page<DishResponseDto>> getDishesByName(@RequestParam String name,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dishService.getDishesByName(name, page, size));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/price")
    public ResponseEntity<Page<DishResponseDto>> getDishesByPriceRange(@RequestParam Double minPrice,
                                                                       @RequestParam Double maxPrice,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dishService.getDishesByPriceRange(minPrice, maxPrice, page, size));
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping
    public ResponseEntity<Void> createDish(@ModelAttribute DishRequestDto dto) {
        dishService.createDish(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateDish(@PathVariable Long id, @RequestBody DishUpdateRequestDto dto) {
        dishService.updateContent(id, dto.getDescription());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }
}