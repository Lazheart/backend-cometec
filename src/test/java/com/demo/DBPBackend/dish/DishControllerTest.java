package com.demo.DBPBackend.dish;

import com.demo.DBPBackend.dish.application.DishController;
import com.demo.DBPBackend.dish.domain.DishService;
import com.demo.DBPBackend.dish.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DishControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DishService dishService;

    @InjectMocks
    private DishController dishController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(dishController).build();
    }

    @Test
    void getAllDishes_ShouldReturnDishes() throws Exception {
        // Arrange
        DishResponseDto dish1 = new DishResponseDto();
        dish1.setDishId(1L);
        dish1.setName("Pizza");
        dish1.setPrice(10.99);
        dish1.setMenuId(1L);

        DishResponseDto dish2 = new DishResponseDto();
        dish2.setDishId(2L);
        dish2.setName("Pasta");
        dish2.setPrice(8.99);
        dish2.setMenuId(1L);

        List<DishResponseDto> dishes = Arrays.asList(dish1, dish2);
        when(dishService.getAllDishes()).thenReturn(dishes);

        // Act & Assert
        mockMvc.perform(get("/dishes/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pizza"))
                .andExpect(jsonPath("$[1].name").value("Pasta"));
    }

    @Test
    void getDishById_ShouldReturnDish() throws Exception {
        // Arrange
        DishResponseDto dish = new DishResponseDto();
        dish.setDishId(1L);
        dish.setName("Pizza");
        dish.setPrice(10.99);
        dish.setMenuId(1L);

        when(dishService.getDishById(1L)).thenReturn(dish);

        // Act & Assert
        mockMvc.perform(get("/dishes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizza"));
    }

    @Test
    void getDishesByMenu_ShouldReturnDishes() throws Exception {
        // Arrange
        DishSummaryDto dish1 = new DishSummaryDto();
        dish1.setId(1L);
        dish1.setName("Pizza");
        dish1.setDescription("Delicious pizza");
        dish1.setPrice(10.99);

        DishSummaryDto dish2 = new DishSummaryDto();
        dish2.setId(2L);
        dish2.setName("Pasta");
        dish2.setDescription("Tasty pasta");
        dish2.setPrice(8.99);

        List<DishSummaryDto> dishes = Arrays.asList(dish1, dish2);
        when(dishService.getDishesByMenuId(1L)).thenReturn(dishes);

        // Act & Assert
        mockMvc.perform(get("/dishes/carta/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pizza"))
                .andExpect(jsonPath("$[1].name").value("Pasta"));
    }

    @Test
    void getDishesByRestaurant_ShouldReturnDishes() throws Exception {
        // Arrange
        DishResponseDto dish1 = new DishResponseDto();
        dish1.setDishId(1L);
        dish1.setName("Pizza");
        dish1.setPrice(10.99);
        dish1.setMenuId(1L);

        DishResponseDto dish2 = new DishResponseDto();
        dish2.setDishId(2L);
        dish2.setName("Pasta");
        dish2.setPrice(8.99);
        dish2.setMenuId(1L);

        List<DishResponseDto> dishes = Arrays.asList(dish1, dish2);
        when(dishService.getDishesByRestaurantId(1L)).thenReturn(dishes);

        // Act & Assert
        mockMvc.perform(get("/dishes/restaurant/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pizza"))
                .andExpect(jsonPath("$[1].name").value("Pasta"));
    }

    @Test
    void createDish_ShouldReturnCreated() throws Exception {
        // Arrange
        DishRequestDto requestDto = new DishRequestDto();
        requestDto.setName("Pizza");
        requestDto.setPrice(10.99);
        requestDto.setDescription("Delicious pizza");
        requestDto.setMenuId(1L);

        // Configuración para método void
        doNothing().when(dishService).createDish(any(DishRequestDto.class));

        // Act & Assert
        mockMvc.perform(post("/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        // Verifica que se llamó al método
        verify(dishService, times(1)).createDish(any(DishRequestDto.class));
    }

    @Test
    void updateDish_ShouldReturnOk() throws Exception {
        // Arrange
        DishUpdateRequestDto updateDto = new DishUpdateRequestDto();
        updateDto.setDescription("Updated description");

        // Mock para método void (no se usa when())
        doNothing().when(dishService).updateContent(1L, "Updated description");

        // Act & Assert
        mockMvc.perform(patch("/dishes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());

        // Verifica que se llamó al método
        verify(dishService, times(1)).updateContent(1L, "Updated description");
    }

    @Test
    void deleteDish_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(dishService).deleteDish(1L);

        // Act & Assert
        mockMvc.perform(delete("/dishes/1"))
                .andExpect(status().isNoContent());

        verify(dishService, times(1)).deleteDish(1L);
    }
}