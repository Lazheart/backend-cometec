package com.demo.DBPBackend.dish;

import com.demo.DBPBackend.dish.application.DishController;
import com.demo.DBPBackend.dish.domain.DishService;
import com.demo.DBPBackend.dish.dto.*;
import com.demo.DBPBackend.exceptions.ResourceNotFoundException;
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

    private DishResponseDto dishResponseDto;
    private DishSummaryDto dishSummaryDto;
    private DishRequestDto dishRequestDto;
    private DishUpdateRequestDto dishUpdateRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(dishController)
                .build();

        // Initialize test data
        dishResponseDto = new DishResponseDto();
        dishResponseDto.setDishId(1L);
        dishResponseDto.setName("Pizza");
        dishResponseDto.setPrice(10.99);
        dishResponseDto.setMenuId(1L);

        dishSummaryDto = new DishSummaryDto();
        dishSummaryDto.setId(1L);
        dishSummaryDto.setName("Pizza");
        dishSummaryDto.setDescription("Delicious pizza");
        dishSummaryDto.setPrice(10.99);

        dishRequestDto = new DishRequestDto();
        dishRequestDto.setName("Pizza");
        dishRequestDto.setPrice(10.99);
        dishRequestDto.setDescription("Delicious pizza");
        dishRequestDto.setMenuId(1L);

        dishUpdateRequestDto = new DishUpdateRequestDto();
        dishUpdateRequestDto.setDescription("Updated description");
    }

    @Test
    void getAllDishes_ShouldReturnListOfDishes() throws Exception {
        // Arrange
        List<DishResponseDto> dishes = Arrays.asList(dishResponseDto);
        when(dishService.getAllDishes()).thenReturn(dishes);

        // Act & Assert
        mockMvc.perform(get("/dishes/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dishId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Pizza"))
                .andExpect(jsonPath("$[0].price").value(10.99));

        verify(dishService, times(1)).getAllDishes();
    }

    @Test
    void getDishById_ShouldReturnDish() throws Exception {
        // Arrange
        when(dishService.getDishById(1L)).thenReturn(dishResponseDto);

        // Act & Assert
        mockMvc.perform(get("/dishes/{dishId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dishId").value(1L))
                .andExpect(jsonPath("$.name").value("Pizza"));

        verify(dishService, times(1)).getDishById(1L);
    }

    @Test
    void getDishById_ShouldReturnNotFoundWhenDishDoesNotExist() throws Exception {
        // Arrange
        when(dishService.getDishById(anyLong())).thenThrow(new ResourceNotFoundException("Dish not found"));

        // Act & Assert
        mockMvc.perform(get("/dishes/{dishId}", 99L))
                .andExpect(status().isNotFound());

        verify(dishService, times(1)).getDishById(99L);
    }

    @Test
    void getDishesByMenu_ShouldReturnListOfDishes() throws Exception {
        // Arrange
        List<DishSummaryDto> dishes = Arrays.asList(dishSummaryDto);
        when(dishService.getDishesByMenuId(1L)).thenReturn(dishes);

        // Act & Assert
        mockMvc.perform(get("/dishes/carta/{menuId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Pizza"));

        verify(dishService, times(1)).getDishesByMenuId(1L);
    }

    @Test
    void getDishesByRestaurant_ShouldReturnListOfDishes() throws Exception {
        // Arrange
        List<DishResponseDto> dishes = Arrays.asList(dishResponseDto);
        when(dishService.getDishesByRestaurantId(1L)).thenReturn(dishes);

        // Act & Assert
        mockMvc.perform(get("/dishes/restaurant/{restaurantId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dishId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Pizza"));

        verify(dishService, times(1)).getDishesByRestaurantId(1L);
    }

    @Test
    void createDish_ShouldReturnCreatedStatus() throws Exception {
        // Arrange
        doNothing().when(dishService).createDish(any(DishRequestDto.class));

        // Act & Assert
        mockMvc.perform(post("/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishRequestDto)))
                .andExpect(status().isCreated());

        verify(dishService, times(1)).createDish(any(DishRequestDto.class));
    }

    @Test
    void updateDish_ShouldReturnOkStatus() throws Exception {
        // Arrange
        doNothing().when(dishService).updateContent(eq(1L), anyString());

        // Act & Assert
        mockMvc.perform(patch("/dishes/{dishId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishUpdateRequestDto)))
                .andExpect(status().isOk());

        verify(dishService, times(1)).updateContent(eq(1L), eq("Updated description"));
    }

    @Test
    void deleteDish_ShouldReturnNoContentStatus() throws Exception {
        // Arrange
        doNothing().when(dishService).deleteDish(1L);

        // Act & Assert
        mockMvc.perform(delete("/dishes/{dishId}", 1L))
                .andExpect(status().isNoContent());

        verify(dishService, times(1)).deleteDish(1L);
    }

    @Test
    void deleteDish_ShouldReturnNotFoundWhenDishDoesNotExist() throws Exception {
        // Arrange
        doThrow(new ResourceNotFoundException("Dish not found")).when(dishService).deleteDish(99L);

        // Act & Assert
        mockMvc.perform(delete("/dishes/{dishId}", 99L))
                .andExpect(status().isNotFound());

        verify(dishService, times(1)).deleteDish(99L);
    }
}