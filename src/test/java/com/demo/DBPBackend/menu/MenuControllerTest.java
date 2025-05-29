package com.demo.DBPBackend.menu;

import com.demo.DBPBackend.menu.application.MenuController;
import com.demo.DBPBackend.menu.domain.MenuService;
import com.demo.DBPBackend.menu.dto.MenuRequestDto;
import com.demo.DBPBackend.menu.dto.MenuResponseDto;
import com.demo.DBPBackend.dish.dto.DishSummaryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuControllerTest {

    @Mock
    private MenuService menuService;

    @InjectMocks
    private MenuController menuController;

    private MenuRequestDto requestDto;
    private MenuResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = new MenuRequestDto();
        requestDto.setRestaurantId(1L);

        responseDto = new MenuResponseDto();
        responseDto.setId(1L);
        responseDto.setRestaurantId(1L);
        responseDto.setRestaurantName("Demo Restaurant");
        responseDto.setDishes(Collections.singletonList(new DishSummaryDto()));
    }

    @Test
    void testCreateMenu() {
        ResponseEntity<Void> response = menuController.createMenu(1L, requestDto);
        verify(menuService).createMenu(requestDto);
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void testGetMenuById() {
        when(menuService.getMenuById(1L)).thenReturn(responseDto);

        ResponseEntity<MenuResponseDto> response = menuController.getMenuById(1L);

        verify(menuService).getMenuById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    void testUpdateMenu() {
        ResponseEntity<Void> response = menuController.updateMenu(1L, requestDto);

        verify(menuService).updateMenu(1L, requestDto);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeleteMenu() {
        ResponseEntity<Void> response = menuController.deleteMenu(1L);

        verify(menuService).deleteMenu(1L);
        assertEquals(204, response.getStatusCodeValue());
    }
}
