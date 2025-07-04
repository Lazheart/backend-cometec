package com.demo.DBPBackend.menu.domain;

import com.demo.DBPBackend.auth.utils.AuthUtils;
import com.demo.DBPBackend.dish.domain.Dish;
import com.demo.DBPBackend.dish.dto.DishRequestDto;
import com.demo.DBPBackend.dish.dto.DishSummaryDto;
import com.demo.DBPBackend.dish.infrastructure.DishRepository;
import com.demo.DBPBackend.exceptions.ResourceNotFoundException;
import com.demo.DBPBackend.exceptions.UnauthorizedOperationException;
import com.demo.DBPBackend.menu.dto.MenuRequestDto;
import com.demo.DBPBackend.menu.dto.MenuResponseDto;
import com.demo.DBPBackend.menu.infrastructure.MenuRepository;
import com.demo.DBPBackend.restaurant.domain.Restaurant;
import com.demo.DBPBackend.restaurant.infrastructure.RestaurantRepository;
import com.demo.DBPBackend.user.domain.User;
import com.demo.DBPBackend.user.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;
    private final UserRepository userRepository;
    private final AuthUtils authUtils;

    // Métodos de paginación agregados
    public Page<MenuResponseDto> getAllMenus(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Menu> menuPage = menuRepository.findAll(pageable);
        return menuPage.map(this::toMenuResponseDto);
    }

    // Métodos originales sin cambios
    @Transactional
    public MenuResponseDto createMenu(MenuRequestDto dto) {
        String email = authUtils.getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (dto.getRestaurantId() == null) {
            throw new ResourceNotFoundException("Restaurant ID is required");
        }

        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        if (!authUtils.isAdminOrResourceOwner(restaurant.getOwner().getId())) {
            throw new UnauthorizedOperationException("You are not authorized to create a menu for this restaurant");
        }

        // Crear y guardar el menú
        Menu menu = new Menu();
        menu.setRestaurant(restaurant);
        menu = menuRepository.save(menu);

        // 🔥 IMPORTANTE: vincular el menú al restaurante
        restaurant.setMenu(menu);
        restaurantRepository.save(restaurant);

        // Guardar los platos si vienen en el DTO
        if (dto.getDishes() != null && !dto.getDishes().isEmpty()) {
            Menu finalMenu = menu;
            List<Dish> dishes = dto.getDishes().stream()
                    .map(dishDto -> {
                        Dish dish = new Dish();
                        dish.setName(dishDto.getName() != null ? dishDto.getName() : "Unnamed Dish");
                        dish.setDescription(dishDto.getDescription() != null ? dishDto.getDescription() : "");
                        dish.setPrice(dishDto.getPrice() != null ? dishDto.getPrice() : 0.0);
                        dish.setCategory(dishDto.getCategory() != null ? dishDto.getCategory() : com.demo.DBPBackend.dish.domain.DishCategory.OTHER);
                        dish.setMenu(finalMenu);
                        return dish;
                    })
                    .collect(Collectors.toList());
            dishRepository.saveAll(dishes);
        }

        return toMenuResponseDto(menu);
    }

    public MenuResponseDto getMenuById(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));

        MenuResponseDto dto = new MenuResponseDto();
        dto.setId(menu.getId());
        dto.setRestaurantId(menu.getRestaurant().getId());
        dto.setRestaurantName(menu.getRestaurant().getName());

        List<DishSummaryDto> dishSummaries = menu.getDishes().stream()
                .map(this::toDishSummary)
                .collect(Collectors.toList());
        dto.setDishes(dishSummaries);

        return dto;
    }

    public MenuResponseDto getMenuByRestaurantId(Long restaurantId) {
        Menu menu = menuRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found for restaurant with id: " + restaurantId));

        MenuResponseDto dto = new MenuResponseDto();
        dto.setId(menu.getId());
        dto.setRestaurantId(menu.getRestaurant().getId());
        dto.setRestaurantName(menu.getRestaurant().getName());

        List<DishSummaryDto> dishSummaries = menu.getDishes().stream()
                .map(this::toDishSummary)
                .collect(Collectors.toList());
        dto.setDishes(dishSummaries);

        return dto;
    }

    @Transactional
    public MenuResponseDto updateMenu(Long id, MenuRequestDto dto) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));

        if (!authUtils.isAdminOrResourceOwner(menu.getRestaurant().getOwner().getId())) {
            throw new UnauthorizedOperationException("You are not authorized to update this menu");
        }

        // Eliminar los platos anteriores
        dishRepository.deleteAll(menu.getDishes());

        // Crear nuevos platos si existen
        if (dto.getDishes() != null && !dto.getDishes().isEmpty()) {
            List<Dish> newDishes = dto.getDishes().stream()
                    .map(dishDto -> {
                        Dish dish = new Dish();
                        dish.setName(dishDto.getName() != null ? dishDto.getName() : "Unnamed Dish");
                        dish.setDescription(dishDto.getDescription() != null ? dishDto.getDescription() : "");
                        dish.setPrice(dishDto.getPrice() != null ? dishDto.getPrice() : 0.0);
                        dish.setCategory(dishDto.getCategory() != null ? dishDto.getCategory() : com.demo.DBPBackend.dish.domain.DishCategory.OTHER);
                        dish.setMenu(menu);
                        return dish;
                    })
                    .collect(Collectors.toList());

            dishRepository.saveAll(newDishes);
        }

        return toMenuResponseDto(menu);
    }

    @Transactional
    public void deleteMenu(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));

        if (!authUtils.isAdminOrResourceOwner(menu.getRestaurant().getOwner().getId())) {
            throw new UnauthorizedOperationException("You are not authorized to delete this menu");
        }

        menuRepository.delete(menu);
    }

    private DishSummaryDto toDishSummary(Dish dish) {
        DishSummaryDto dto = new DishSummaryDto();
        dto.setId(dish.getId());
        dto.setName(dish.getName());
        dto.setPrice(dish.getPrice());
        dto.setCategory(dish.getCategory());
        return dto;
    }

    private MenuResponseDto toMenuResponseDto(Menu menu) {
        MenuResponseDto dto = new MenuResponseDto();
        dto.setId(menu.getId());
        dto.setRestaurantId(menu.getRestaurant().getId());
        dto.setRestaurantName(menu.getRestaurant().getName());
        
        // Mapear las entidades Dish a DTOs DishSummaryDto
        List<DishSummaryDto> dishSummaries = menu.getDishes().stream()
            .map(this::toDishSummary)
            .collect(Collectors.toList());
        dto.setDishes(dishSummaries);
        
        return dto;
    }


}