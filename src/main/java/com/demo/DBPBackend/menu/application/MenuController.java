package com.demo.DBPBackend.menu.application;

import com.demo.DBPBackend.menu.domain.MenuService;
import com.demo.DBPBackend.menu.dto.MenuRequestDto;
import com.demo.DBPBackend.menu.dto.MenuResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus")
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<Page<MenuResponseDto>> getAllMenus(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(menuService.getAllMenus(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuResponseDto> getMenuById(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.getMenuById(id));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<MenuResponseDto> createMenu(@RequestBody MenuRequestDto menuRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuService.createMenu(menuRequestDto));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MenuResponseDto> updateMenu(@PathVariable Long id,
                                                      @RequestBody MenuRequestDto menuRequestDto) {
        return ResponseEntity.ok(menuService.updateMenu(id, menuRequestDto));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }
}
