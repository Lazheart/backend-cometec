package com.demo.DBPBackend.restaurant.domain;

import com.demo.DBPBackend.auth.utils.AuthUtils;
import com.demo.DBPBackend.comment.dto.CommentResponseDto;
import com.demo.DBPBackend.exceptions.InvalidCategoryException;
import com.demo.DBPBackend.exceptions.ResourceNotFoundException;
import com.demo.DBPBackend.exceptions.UnauthorizedOperationException;
import com.demo.DBPBackend.localMediaStorage.domain.MediaStorageService;
import com.demo.DBPBackend.location.domain.Location;
import com.demo.DBPBackend.location.dto.LocationDto;
import com.demo.DBPBackend.menu.dto.MenuResponseDto;
import com.demo.DBPBackend.restaurant.dto.RestaurantRequestDto;
import com.demo.DBPBackend.restaurant.dto.RestaurantResponseDto;
import com.demo.DBPBackend.restaurant.dto.RestaurantSummaryDto;
import com.demo.DBPBackend.restaurant.dto.RestaurantWithReviewsDto;
import com.demo.DBPBackend.restaurant.infrastructure.RestaurantRepository;
import com.demo.DBPBackend.review.domain.Review;
import com.demo.DBPBackend.review.dto.ReviewResponseDto;
import com.demo.DBPBackend.user.domain.Role;
import com.demo.DBPBackend.user.domain.User;
import com.demo.DBPBackend.user.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final AuthUtils authUtils;
    private final MediaStorageService mediaStorageService;

    public Page<RestaurantSummaryDto> getAllRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurants = restaurantRepository.findAll(pageable);
        return restaurants.map(this::toRestaurantSummary);
    }

    public Page<RestaurantSummaryDto> getRestaurantsByName(String name, int page, int size) {
        String normalizedName = normalizeText(name);
        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurants = restaurantRepository.findByNameContaining(normalizedName, pageable);
        return restaurants.map(this::toRestaurantSummary);
    }

    public Page<RestaurantSummaryDto> getRestaurantsByOwnerId(Long ownerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurants = restaurantRepository.findByOwnerId(ownerId, pageable);
        return restaurants.map(this::toRestaurantSummary);
    }

    public Page<RestaurantSummaryDto> getAllRestaurantsOrderedByName(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurants = restaurantRepository.findAllByOrderByNameAsc(pageable);
        return restaurants.map(this::toRestaurantSummary);
    }

    public Page<RestaurantSummaryDto> getAllRestaurantsOrderedById(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurants = restaurantRepository.findAllByOrderByIdDesc(pageable);
        return restaurants.map(this::toRestaurantSummary);
    }

    public Page<RestaurantSummaryDto> getRestaurantsByCategory(RestaurantCategory category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurants = restaurantRepository.findByCategory(category, pageable);
        return restaurants.map(this::toRestaurantSummary);
    }

    public Page<RestaurantSummaryDto> getRestaurantsByCategoryOrderedByName(RestaurantCategory category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurants = restaurantRepository.findByCategoryOrderByNameAsc(category, pageable);
        return restaurants.map(this::toRestaurantSummary);
    }

    public Page<RestaurantSummaryDto> getRestaurantsByCategoryOrderedById(RestaurantCategory category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurants = restaurantRepository.findByCategoryOrderByIdDesc(category, pageable);
        return restaurants.map(this::toRestaurantSummary);
    }

    public RestaurantResponseDto getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        return toRestaurantResponse(restaurant);
    }

    public Page<ReviewResponseDto> getRestaurantReviews(Long id, int page, int size) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        // Como no hay método directo en el repositorio, usamos stream con paginación manual
        List<ReviewResponseDto> allReviews = restaurant.getValoraciones().stream()
                .map(this::toReviewResponse)
                .collect(Collectors.toList());
        
        // Paginación manual
        int start = page * size;
        int end = Math.min(start + size, allReviews.size());
        
        if (start >= allReviews.size()) {
            return Page.empty(PageRequest.of(page, size));
        }
        
        List<ReviewResponseDto> pageContent = allReviews.subList(start, end);
        return new PageImpl<>(pageContent, PageRequest.of(page, size), allReviews.size());
    }

    public Page<CommentResponseDto> getRestaurantComments(Long id, int page, int size) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        // Como no hay método directo en el repositorio, usamos stream con paginación manual
        List<CommentResponseDto> allComments = restaurant.getValoraciones().stream()
                .flatMap(review -> review.getComments().stream())
                .map(this::toCommentResponse)
                .collect(Collectors.toList());
        
        // Paginación manual
        int start = page * size;
        int end = Math.min(start + size, allComments.size());
        
        if (start >= allComments.size()) {
            return Page.empty(PageRequest.of(page, size));
        }
        
        List<CommentResponseDto> pageContent = allComments.subList(start, end);
        return new PageImpl<>(pageContent, PageRequest.of(page, size), allComments.size());
    }

    public MenuResponseDto getRestaurantMenu(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        if (restaurant.getMenu() == null) {
            throw new ResourceNotFoundException("Restaurant does not have a menu yet");
        }

        MenuResponseDto dto = new MenuResponseDto();
        dto.setId(restaurant.getMenu().getId());
        dto.setRestaurantId(restaurant.getId());
        dto.setRestaurantName(restaurant.getName());
        dto.setDishes(restaurant.getMenu().getDishes().stream()
                .map(dish -> {
                    var dishDto = new com.demo.DBPBackend.dish.dto.DishSummaryDto();
                    dishDto.setId(dish.getId());
                    dishDto.setName(dish.getName());
                    dishDto.setPrice(dish.getPrice());
                    return dishDto;
                })
                .collect(Collectors.toList()));

        return dto;
    }

    @Transactional
    public RestaurantResponseDto createRestaurant(RestaurantRequestDto dto) {
        String email = authUtils.getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Cambiar el rol a OWNER si es USER
        if (currentUser.getRole() == Role.USER) {
            currentUser.setRole(Role.OWNER);
            userRepository.save(currentUser);
        }

        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new ResourceNotFoundException("Restaurant name is required");
        }

        if (dto.getLocationCreateDto() == null) {
            throw new ResourceNotFoundException("Location is required");
        }

        Restaurant restaurant = new Restaurant();
        restaurant.setName(dto.getName());
        restaurant.setCategory(dto.getCategory() != null ? dto.getCategory() : RestaurantCategory.OTHER);
        restaurant.setOwner(currentUser);

        Location location = new Location();
        location.setLatitud(dto.getLocationCreateDto().getLatitud() != null ? dto.getLocationCreateDto().getLatitud() : 0.0);
        location.setLongitud(dto.getLocationCreateDto().getLongitud() != null ? dto.getLocationCreateDto().getLongitud() : 0.0);
        restaurant.setLocation(location);

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            try {
                String imageUrl = mediaStorageService.uploadFile(dto.getImage());
                restaurant.setImageUrl(imageUrl);
            } catch (FileUploadException e) {
                throw new RuntimeException("Error uploading restaurant image: " + e.getMessage());
            }
        }

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return toRestaurantResponse(savedRestaurant);
    }

    @Transactional
    public void updateRestaurant(Long id, RestaurantRequestDto dto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        if (!authUtils.isAdminOrResourceOwner(restaurant.getOwner().getId())) {
            throw new UnauthorizedOperationException("You are not authorized to update this restaurant");
        }

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            restaurant.setName(dto.getName());
        }
        
        if (dto.getCategory() != null) {
            restaurant.setCategory(dto.getCategory());
        }

        if (dto.getLocationCreateDto() != null) {
            Location location = restaurant.getLocation();
            if (dto.getLocationCreateDto().getLatitud() != null) {
                location.setLatitud(dto.getLocationCreateDto().getLatitud());
            }
            if (dto.getLocationCreateDto().getLongitud() != null) {
                location.setLongitud(dto.getLocationCreateDto().getLongitud());
            }
        }

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            try {
                String imageUrl = mediaStorageService.uploadFile(dto.getImage());
                restaurant.setImageUrl(imageUrl);
            } catch (FileUploadException e) {
                throw new RuntimeException("Error uploading restaurant image: " + e.getMessage());
            }
        }

        restaurantRepository.save(restaurant);
    }

    @Transactional
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        if (!authUtils.isAdminOrResourceOwner(restaurant.getOwner().getId())) {
            throw new UnauthorizedOperationException("You are not authorized to delete this restaurant");
        }

        restaurantRepository.delete(restaurant);
    }

    private String normalizeText(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFC)
                .replaceAll("[''']", "'")
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }

    private RestaurantSummaryDto toRestaurantSummary(Restaurant restaurant) {
        RestaurantSummaryDto dto = new RestaurantSummaryDto();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setCategory(restaurant.getCategory());
        dto.setOwnerId(restaurant.getOwner().getId());
        dto.setOwnerName(restaurant.getOwner().getName());
        dto.setLocationDto(toLocationDto(restaurant.getLocation()));
        dto.setTotalReviews(restaurant.getValoraciones().size());
        dto.setHasMenu(restaurant.getMenu() != null);
        dto.setImageUrl(restaurant.getImageUrl());
        return dto;
    }

    private RestaurantResponseDto toRestaurantResponse(Restaurant restaurant) {
        RestaurantResponseDto dto = new RestaurantResponseDto();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setCategory(restaurant.getCategory());
        dto.setOwnerId(restaurant.getOwner().getId());
        dto.setOwnerName(restaurant.getOwner().getName());
        dto.setLocationDto(toLocationDto(restaurant.getLocation()));
        dto.setTotalReviews(restaurant.getValoraciones().size());
        dto.setHasMenu(restaurant.getMenu() != null);
        dto.setImageUrl(restaurant.getImageUrl());
        return dto;
    }

    private ReviewResponseDto toReviewResponse(Review review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(review.getId());
        dto.setContent(review.getContent());
        dto.setRestaurantId(review.getRestaurant().getId());
        dto.setUserId(review.getUser().getId());
        dto.setUserName(review.getUser().getName());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }

    private CommentResponseDto toCommentResponse(com.demo.DBPBackend.comment.domain.Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setReviewId(comment.getReview().getId());
        dto.setUserId(comment.getUser().getId());
        dto.setUserName(comment.getUser().getName());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }

    private LocationDto toLocationDto(Location location) {
        LocationDto dto = new LocationDto();
        dto.setLatitud(location.getLatitud());
        dto.setLongitud(location.getLongitud());
        return dto;
    }

    /**
     * Valida si una categoría es válida y la convierte a RestaurantCategory
     * @param categoryString La categoría como string
     * @return RestaurantCategory válido
     * @throws InvalidCategoryException si la categoría no es válida
     */
    public RestaurantCategory validateAndParseCategory(String categoryString) {
        try {
            return RestaurantCategory.valueOf(categoryString.toUpperCase());
        } catch (IllegalArgumentException e) {
            String validCategories = String.join(", ", 
                java.util.Arrays.stream(RestaurantCategory.values())
                    .map(Enum::name)
                    .toArray(String[]::new));
            throw new InvalidCategoryException(categoryString, validCategories);
        }
    }

    /**
     * Devuelve el top 3 de restaurantes con mejor valoración promedio.
     * Solo incluye id, nombre, categoría e imagen.
     */
    public List<RestaurantSummaryDto> getTop3RestaurantsByRating() {
        // Obtener todos los restaurantes
        List<Restaurant> allRestaurants = restaurantRepository.findAll();
        // Calcular el promedio de valoraciones para cada restaurante
        return allRestaurants.stream()
                .filter(r -> r.getValoraciones() != null && !r.getValoraciones().isEmpty())
                .sorted((r1, r2) -> {
                    double avg1 = r1.getValoraciones().stream().mapToDouble(Review::getCalificacion).average().orElse(0);
                    double avg2 = r2.getValoraciones().stream().mapToDouble(Review::getCalificacion).average().orElse(0);
                    return Double.compare(avg2, avg1); // Descendente
                })
                .limit(3)
                .map(this::toRestaurantSummary)
                .collect(Collectors.toList());
    }

    public Page<RestaurantSummaryDto> searchRestaurants(String name, String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        boolean hasName = name != null && !name.isBlank();
        boolean hasCategory = category != null && !category.isBlank();
        Page<Restaurant> restaurants;
        if (hasName && hasCategory) {
            RestaurantCategory cat = validateAndParseCategory(category);
            String normalizedName = normalizeText(name);
            restaurants = restaurantRepository.findByNameContainingAndCategory(normalizedName, cat, pageable);
        } else if (hasName) {
            String normalizedName = normalizeText(name);
            restaurants = restaurantRepository.findByNameContaining(normalizedName, pageable);
        } else if (hasCategory) {
            RestaurantCategory cat = validateAndParseCategory(category);
            restaurants = restaurantRepository.findByCategory(cat, pageable);
        } else {
            restaurants = restaurantRepository.findAll(pageable);
        }
        return restaurants.map(this::toRestaurantSummary);
    }

    public List<RestaurantWithReviewsDto> findRestaurantsByNameWithReviews(String name) {
        String normalizedName = normalizeText(name);
        List<Restaurant> restaurants = restaurantRepository.findAll().stream()
                .filter(r -> r.getName().toLowerCase().contains(normalizedName.toLowerCase()))
                .collect(Collectors.toList());
        return restaurants.stream().map(r -> {
            RestaurantWithReviewsDto dto = new RestaurantWithReviewsDto();
            dto.setId(r.getId());
            dto.setName(r.getName());
            dto.setCategory(r.getCategory());
            dto.setReviews(r.getValoraciones().stream().map(this::toReviewResponse).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

    public List<RestaurantWithReviewsDto> findRestaurantsByCategoryWithReviews(String category) {
        RestaurantCategory cat = validateAndParseCategory(category);
        List<Restaurant> restaurants = restaurantRepository.findAll().stream()
                .filter(r -> r.getCategory() == cat)
                .collect(Collectors.toList());
        return restaurants.stream().map(r -> {
            RestaurantWithReviewsDto dto = new RestaurantWithReviewsDto();
            dto.setId(r.getId());
            dto.setName(r.getName());
            dto.setCategory(r.getCategory());
            dto.setReviews(r.getValoraciones().stream().map(this::toReviewResponse).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

    public List<RestaurantWithReviewsDto> findRestaurantsByNameAndCategoryWithReviews(String name, String category) {
        RestaurantCategory cat = validateAndParseCategory(category);
        String normalizedName = normalizeText(name);
        List<Restaurant> restaurants = restaurantRepository.findAll().stream()
                .filter(r -> r.getCategory() == cat && r.getName().toLowerCase().contains(normalizedName.toLowerCase()))
                .collect(Collectors.toList());
        return restaurants.stream().map(r -> {
            RestaurantWithReviewsDto dto = new RestaurantWithReviewsDto();
            dto.setId(r.getId());
            dto.setName(r.getName());
            dto.setCategory(r.getCategory());
            dto.setReviews(r.getValoraciones().stream().map(this::toReviewResponse).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }
}