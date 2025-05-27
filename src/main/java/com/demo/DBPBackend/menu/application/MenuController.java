package com.demo.DBPBackend.menu.application;

import com.demo.DBPBackend.menu.domain.MenuService;
import com.demo.DBPBackend.menu.dto.MenuRequestDto;
import com.demo.DBPBackend.menu.dto.MenuResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // Crear carta para restaurante
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PostMapping("/restaurants/{restaurantId}")
    public ResponseEntity<Void> createMenu(@PathVariable Long restaurantId, @RequestBody MenuRequestDto dto) {
        dto.setRestaurantId(restaurantId);
        menuService.createMenu(dto);
        return ResponseEntity.status(201).build();
    }

    // Ver carta específica
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_OWNER', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<MenuResponseDto> getMenuById(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.getMenuById(id));
    }

    // Actualizar carta
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMenu(@PathVariable Long id, @RequestBody MenuRequestDto dto) {
        menuService.updateMenu(id, dto);
        return ResponseEntity.ok().build();
    }

    // Eliminar carta
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }
}
