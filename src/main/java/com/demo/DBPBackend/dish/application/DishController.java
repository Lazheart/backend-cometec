package com.demo.DBPBackend.dish.application;

import com.demo.DBPBackend.dish.domain.DishService;
import com.demo.DBPBackend.dish.domain.DishCategory;
import com.demo.DBPBackend.dish.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dishes")
public class DishController {

    private final DishService dishService;

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<DishSummaryDto>> getDishesByCategory(@PathVariable DishCategory category,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dishService.getDishesByCategory(category, page, size));
    }
//verificar que no se repita
    @GetMapping("/menus/{menuId}/dishes")
    public ResponseEntity<Page<DishResponseDto>> getDishesByMenu(@PathVariable Long menuId,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dishService.getDishesByMenuId(menuId, page, size));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<DishResponseDto> createDish(@ModelAttribute DishRequestDto dishRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dishService.createDish(dishRequestDto));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<DishResponseDto> updateDish(@PathVariable Long id, @ModelAttribute DishUpdateRequestDto dishUpdateRequestDto) {
        return ResponseEntity.ok(dishService.updateDish(id, dishUpdateRequestDto));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }

    //Muestra las 4-5 categorias de dishZes
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'USER')")
    @GetMapping("/categories")
    public ResponseEntity<DishCategory[]> getDishCategories() {
        return ResponseEntity.ok(DishCategory.values());
    }
}