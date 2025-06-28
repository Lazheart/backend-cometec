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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Normalizer;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    public Page<DishResponseDto> getAllDishes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findAll(pageable);
        return dishPage.map(this::toResponseDto);
    }

    public Page<DishResponseDto> getAllDishesOrderedByName(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findAllByOrderByNameAsc(pageable);
        return dishPage.map(this::toResponseDto);
    }

    public Page<DishResponseDto> getAllDishesOrderedByPriceAsc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findAllByOrderByPriceAsc(pageable);
        return dishPage.map(this::toResponseDto);
    }

    public Page<DishResponseDto> getAllDishesOrderedByPriceDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findAllByOrderByPriceDesc(pageable);
        return dishPage.map(this::toResponseDto);
    }

    public DishResponseDto getDishById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));
        return toResponseDto(dish);
    }

    public Page<DishSummaryDto> getDishesByMenuId(Long menuId, int page, int size) {
        if (!menuRepository.existsById(menuId)) {
            throw new ResourceNotFoundException("Menu not found");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByMenuId(menuId, pageable);
        return dishPage.map(this::toSummaryDto);
    }

    public Page<DishSummaryDto> getDishesByMenuIdOrderedByName(Long menuId, int page, int size) {
        if (!menuRepository.existsById(menuId)) {
            throw new ResourceNotFoundException("Menu not found");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByMenuIdOrderByNameAsc(menuId, pageable);
        return dishPage.map(this::toSummaryDto);
    }

    public Page<DishSummaryDto> getDishesByMenuIdOrderedByPriceAsc(Long menuId, int page, int size) {
        if (!menuRepository.existsById(menuId)) {
            throw new ResourceNotFoundException("Menu not found");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByMenuIdOrderByPriceAsc(menuId, pageable);
        return dishPage.map(this::toSummaryDto);
    }

    public Page<DishSummaryDto> getDishesByMenuIdOrderedByPriceDesc(Long menuId, int page, int size) {
        if (!menuRepository.existsById(menuId)) {
            throw new ResourceNotFoundException("Menu not found");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByMenuIdOrderByPriceDesc(menuId, pageable);
        return dishPage.map(this::toSummaryDto);
    }

    public Page<DishResponseDto> getDishesByName(String name, int page, int size) {
        String normalizedName = normalizeText(name);
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByNameContaining(normalizedName, pageable);
        return dishPage.map(this::toResponseDto);
    }

    public Page<DishResponseDto> getDishesByPriceRange(Double minPrice, Double maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        return dishPage.map(this::toResponseDto);
    }

    public Page<DishResponseDto> getDishesByRestaurantId(Long restaurantId, int page, int size) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        Menu menu = menuRepository.findByRestaurantId(restaurant.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found for restaurant"));

        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByMenuId(menu.getId(), pageable);
        return dishPage.map(this::toResponseDto);
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
        if (!dishRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dish not found");
        }
        dishRepository.deleteById(id);
    }

    private String normalizeText(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFC)
                .replaceAll("[''']", "'")
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }

    private DishResponseDto toResponseDto(Dish dish) {
        DishResponseDto dto = new DishResponseDto();
        dto.setId(dish.getId());
        dto.setName(dish.getName());
        dto.setDescription(dish.getDescription());
        dto.setPrice(dish.getPrice());
        dto.setMenuId(dish.getMenu().getId());
        return dto;
    }

    private DishSummaryDto toSummaryDto(Dish dish) {
        DishSummaryDto dto = new DishSummaryDto();
        dto.setId(dish.getId());
        dto.setName(dish.getName());
        dto.setPrice(dish.getPrice());
        return dto;
    }
}
