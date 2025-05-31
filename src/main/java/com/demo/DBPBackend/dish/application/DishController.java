package com.demo.DBPBackend.dish.application;

import com.demo.DBPBackend.dish.domain.DishService;
import com.demo.DBPBackend.dish.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<DishResponseDto>> getAllDishes() {
        List<DishResponseDto> response = dishService.getAllDishes();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{dishId}")
    public ResponseEntity<DishResponseDto> getDishById(@PathVariable("dishId") Long id) {
        return ResponseEntity.ok(dishService.getDishById(id));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/carta/{menuId}")
    public ResponseEntity<List<DishSummaryDto>> getDishesByMenu(@PathVariable Long menuId) {
        return ResponseEntity.ok(dishService.getDishesByMenuId(menuId));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<DishResponseDto>> getDishesByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(dishService.getDishesByRestaurantId(restaurantId));
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping
    public ResponseEntity<Void> createDish(@ModelAttribute DishRequestDto dto) {
        dishService.createDish(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("/{dishId}")
    public ResponseEntity<DishResponseDto> updateDish(@PathVariable("dishId") Long id, @RequestBody DishUpdateRequestDto dto) {
        dishService.updateContent(id, dto.getDescription());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("/{dishId}")
    public ResponseEntity<Void> deleteDish(@PathVariable("dishId") Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }
}