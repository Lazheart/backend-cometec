package com.demo.DBPBackend.dish.domain;

import com.demo.DBPBackend.dish.dto.DishRequestDto;
import com.demo.DBPBackend.dish.dto.DishResponseDto;
import com.demo.DBPBackend.dish.dto.DishSummaryDto;
import com.demo.DBPBackend.dish.infrastructure.DishRepository;
import com.demo.DBPBackend.exceptions.ResourceNotFoundException;
import com.demo.DBPBackend.menu.domain.Menu;
import com.demo.DBPBackend.menu.infrastructure.MenuRepository;
import com.demo.DBPBackend.restaurant.domain.Restaurant;
import com.demo.DBPBackend.restaurant.infrastructure.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    public List<DishResponseDto> getAllDishes() {
        return dishRepository.findAll().stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    public DishResponseDto getDishById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));
        return toResponseDto(dish);
    }

    public List<DishSummaryDto> getDishesByMenuId(Long menuId) {
        if (!menuRepository.existsById(menuId)) {
            throw new ResourceNotFoundException("Menu not found");
        }
        return dishRepository.findByMenuId(menuId).stream().map(this::toSummaryDto).collect(Collectors.toList());
    }

    public List<DishResponseDto> getDishesByRestaurantId(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        Menu menu = menuRepository.findByRestaurantId(restaurant.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found for restaurant"));

        return dishRepository.findByMenuId(menu.getId()).stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    @Transactional
    public void createDish(DishRequestDto dto) {
        Menu menu = menuRepository.findById(dto.getMenuId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));

        Dish dish = new Dish();
        dish.setName(dto.getName());
        dish.setDescription(dto.getDescription());
        dish.setPrice(dto.getPrice());
        dish.setMenu(menu);

        dishRepository.save(dish);
    }

    @Transactional
    public void updateContent(Long id, String description) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));
        dish.setDescription(description);
        dishRepository.save(dish);
    }

    @Transactional
    public void deleteDish(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));
        dishRepository.delete(dish);
    }

    private DishResponseDto toResponseDto(Dish dish) {
        DishResponseDto dto = new DishResponseDto();
        dto.setDishId(dish.getId());
        dto.setName(dish.getName());
        dto.setPrice(dish.getPrice());
        dto.setMenuId(dish.getMenu().getId());
        return dto;
    }

    private DishSummaryDto toSummaryDto(Dish dish) {
        DishSummaryDto dto = new DishSummaryDto();
        dto.setId(dish.getId());
        dto.setName(dish.getName());
        dto.setDescription(dish.getDescription());
        dto.setPrice(dish.getPrice());
        return dto;
    }

}
