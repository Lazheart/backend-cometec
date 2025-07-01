package com.demo.DBPBackend.dish.domain;

import com.demo.DBPBackend.auth.utils.AuthUtils;
import com.demo.DBPBackend.dish.dto.DishRequestDto;
import com.demo.DBPBackend.dish.dto.DishResponseDto;
import com.demo.DBPBackend.dish.dto.DishSummaryDto;
import com.demo.DBPBackend.dish.dto.DishUpdateRequestDto;
import com.demo.DBPBackend.dish.infrastructure.DishRepository;
import com.demo.DBPBackend.exceptions.ResourceNotFoundException;
import com.demo.DBPBackend.exceptions.UnauthorizedOperationException;
import com.demo.DBPBackend.menu.domain.Menu;
import com.demo.DBPBackend.menu.infrastructure.MenuRepository;
import com.demo.DBPBackend.restaurant.domain.Restaurant;
import com.demo.DBPBackend.restaurant.infrastructure.RestaurantRepository;
import com.demo.DBPBackend.localMediaStorage.domain.MediaStorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishService {

    private final DishRepository dishRepository;
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final AuthUtils authUtils;
    private final MediaStorageService mediaStorageService;

    public Page<DishSummaryDto> getAllDishes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findAll(pageable);
        return dishPage.map(this::toDishSummaryDto);
    }

    public Page<DishResponseDto> getAllDishesOrderedByName(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findAllByOrderByNameAsc(pageable);
        return dishPage.map(this::toDishResponseDto);
    }

    public Page<DishResponseDto> getAllDishesOrderedByPrice(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findAllByOrderByPriceAsc(pageable);
        return dishPage.map(this::toDishResponseDto);
    }

    public Page<DishResponseDto> getAllDishesOrderedByPriceAsc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findAllByOrderByPriceAsc(pageable);
        return dishPage.map(this::toDishResponseDto);
    }

    public Page<DishResponseDto> getAllDishesOrderedByPriceDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findAllByOrderByPriceDesc(pageable);
        return dishPage.map(this::toDishResponseDto);
    }

    public DishResponseDto getDishById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));
        return toDishResponseDto(dish);
    }

    public Page<DishResponseDto> getDishesByMenuId(Long menuId, int page, int size) {
        if (!menuRepository.existsById(menuId)) {
            throw new ResourceNotFoundException("Menu not found");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByMenuId(menuId, pageable);
        return dishPage.map(this::toDishResponseDto);
    }

    public Page<DishSummaryDto> getDishesByMenuIdOrderedByName(Long menuId, int page, int size) {
        if (!menuRepository.existsById(menuId)) {
            throw new ResourceNotFoundException("Menu not found");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByMenuIdOrderByNameAsc(menuId, pageable);
        return dishPage.map(this::toDishSummaryDto);
    }

    public Page<DishSummaryDto> getDishesByMenuIdOrderedByPriceAsc(Long menuId, int page, int size) {
        if (!menuRepository.existsById(menuId)) {
            throw new ResourceNotFoundException("Menu not found");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByMenuIdOrderByPriceAsc(menuId, pageable);
        return dishPage.map(this::toDishSummaryDto);
    }

    public Page<DishSummaryDto> getDishesByMenuIdOrderedByPriceDesc(Long menuId, int page, int size) {
        if (!menuRepository.existsById(menuId)) {
            throw new ResourceNotFoundException("Menu not found");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByMenuIdOrderByPriceDesc(menuId, pageable);
        return dishPage.map(this::toDishSummaryDto);
    }

    public Page<DishResponseDto> getDishesByName(String name, int page, int size) {
        String normalizedName = normalizeText(name);
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishes = dishRepository.findByNameContaining(normalizedName, pageable);
        return dishes.map(this::toDishResponseDto);
    }

    public Page<DishResponseDto> getDishesByPriceRange(Double minPrice, Double maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        return dishPage.map(this::toDishResponseDto);
    }

    public Page<DishResponseDto> getDishesByRestaurantId(Long restaurantId, int page, int size) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        Menu menu = menuRepository.findByRestaurantId(restaurant.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found for restaurant"));

        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByMenuId(menu.getId(), pageable);
        return dishPage.map(this::toDishResponseDto);
    }

    @Transactional
    public DishResponseDto createDish(DishRequestDto dto) {
        if (dto.getMenuId() == null) {
            throw new ResourceNotFoundException("Menu ID is required");
        }

        Menu menu = menuRepository.findById(dto.getMenuId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));

        Dish dish = new Dish();
        dish.setName(dto.getName() != null ? dto.getName() : "Unnamed Dish");
        dish.setDescription(dto.getDescription() != null ? dto.getDescription() : "");
        dish.setPrice(dto.getPrice() != null ? dto.getPrice() : 0.0);
        dish.setCategory(dto.getCategory() != null ? dto.getCategory() : DishCategory.OTHER);
        dish.setMenu(menu);

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            try {
                String imageUrl = mediaStorageService.uploadFile(dto.getImage());
                dish.setImageUrl(imageUrl);
            } catch (FileUploadException e) {
                throw new RuntimeException("Error uploading dish image: " + e.getMessage());
            }
        }

        Dish savedDish = dishRepository.save(dish);
        return toDishResponseDto(savedDish);
    }

    @Transactional
    public DishResponseDto updateDish(Long id, DishUpdateRequestDto dto) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));
        
        if (dto.getName() != null && !dto.getName().isEmpty()) {
            dish.setName(dto.getName());
        }
        
        if (dto.getDescription() != null && !dto.getDescription().isEmpty()) {
            dish.setDescription(dto.getDescription());
        }
        
        if (dto.getPrice() != null && dto.getPrice() >= 0) {
            dish.setPrice(dto.getPrice());
        }
        
        if (dto instanceof DishRequestDto) {
            DishRequestDto req = (DishRequestDto) dto;
            if (req.getImage() != null && !req.getImage().isEmpty()) {
                try {
                    String imageUrl = mediaStorageService.uploadFile(req.getImage());
                    dish.setImageUrl(imageUrl);
                } catch (FileUploadException e) {
                    throw new RuntimeException("Error uploading dish image: " + e.getMessage());
                }
            }
        }
        
        Dish savedDish = dishRepository.save(dish);
        return toDishResponseDto(savedDish);
    }

    @Transactional
    public void deleteDish(Long id) {
        if (!dishRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dish not found");
        }
        dishRepository.deleteById(id);
    }

    public Page<DishSummaryDto> getDishesByCategory(DishCategory category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByCategory(category, pageable);
        return dishPage.map(this::toDishSummaryDto);
    }

    public Page<DishResponseDto> getDishesByCategoryOrderedByName(DishCategory category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByCategoryOrderByNameAsc(category, pageable);
        return dishPage.map(this::toDishResponseDto);
    }

    public Page<DishResponseDto> getDishesByCategoryOrderedByPriceAsc(DishCategory category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByCategoryOrderByPriceAsc(category, pageable);
        return dishPage.map(this::toDishResponseDto);
    }

    public Page<DishResponseDto> getDishesByCategoryOrderedByPriceDesc(DishCategory category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByCategoryOrderByPriceDesc(category, pageable);
        return dishPage.map(this::toDishResponseDto);
    }

    public Page<DishSummaryDto> getDishesByMenuIdAndCategory(Long menuId, DishCategory category, int page, int size) {
        if (!menuRepository.existsById(menuId)) {
            throw new ResourceNotFoundException("Menu not found");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByMenuIdAndCategory(menuId, category, pageable);
        return dishPage.map(this::toDishSummaryDto);
    }

    public Page<DishSummaryDto> getDishesByMenuIdAndCategoryOrderedByName(Long menuId, DishCategory category, int page, int size) {
        if (!menuRepository.existsById(menuId)) {
            throw new ResourceNotFoundException("Menu not found");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByMenuIdAndCategoryOrderByNameAsc(menuId, category, pageable);
        return dishPage.map(this::toDishSummaryDto);
    }

    public Page<DishSummaryDto> getDishesByMenuIdAndCategoryOrderedByPriceAsc(Long menuId, DishCategory category, int page, int size) {
        if (!menuRepository.existsById(menuId)) {
            throw new ResourceNotFoundException("Menu not found");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByMenuIdAndCategoryOrderByPriceAsc(menuId, category, pageable);
        return dishPage.map(this::toDishSummaryDto);
    }

    public Page<DishSummaryDto> getDishesByMenuIdAndCategoryOrderedByPriceDesc(Long menuId, DishCategory category, int page, int size) {
        if (!menuRepository.existsById(menuId)) {
            throw new ResourceNotFoundException("Menu not found");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Dish> dishPage = dishRepository.findByMenuIdAndCategoryOrderByPriceDesc(menuId, category, pageable);
        return dishPage.map(this::toDishSummaryDto);
    }

    private String normalizeText(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFC)
                .replaceAll("[''']", "'")
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }

    private DishResponseDto toDishResponseDto(Dish dish) {
        DishResponseDto dto = new DishResponseDto();
        dto.setId(dish.getId());
        dto.setName(dish.getName());
        dto.setDescription(dish.getDescription());
        dto.setPrice(dish.getPrice());
        dto.setCategory(dish.getCategory());
        dto.setMenuId(dish.getMenu().getId());
        dto.setImageUrl(dish.getImageUrl());
        return dto;
    }

    private DishSummaryDto toDishSummaryDto(Dish dish) {
        DishSummaryDto dto = new DishSummaryDto();
        dto.setId(dish.getId());
        dto.setName(dish.getName());
        dto.setDescription(dish.getDescription());
        dto.setPrice(dish.getPrice());
        dto.setCategory(dish.getCategory());
        dto.setImageUrl(dish.getImageUrl());
        return dto;
    }
}
