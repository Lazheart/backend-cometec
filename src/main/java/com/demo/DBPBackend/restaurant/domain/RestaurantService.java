package com.demo.DBPBackend.restaurant.domain;

import com.demo.DBPBackend.auth.utils.AuthUtils;
import com.demo.DBPBackend.comment.dto.CommentResponseDto;
import com.demo.DBPBackend.exceptions.ResourceNotFoundException;
import com.demo.DBPBackend.exceptions.UnauthorizedOperationException;
import com.demo.DBPBackend.menu.dto.MenuResponseDto;
import com.demo.DBPBackend.restaurant.dto.RestaurantRequestDto;
import com.demo.DBPBackend.restaurant.dto.RestaurantResponseDto;
import com.demo.DBPBackend.restaurant.dto.RestaurantSummaryDto;
import com.demo.DBPBackend.restaurant.infrastructure.RestaurantRepository;
import com.demo.DBPBackend.review.domain.Review;
import com.demo.DBPBackend.review.dto.ReviewResponseDto;
import com.demo.DBPBackend.location.domain.Location;
import com.demo.DBPBackend.location.dto.LocationDto;
import com.demo.DBPBackend.user.domain.User;
import com.demo.DBPBackend.user.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
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
        Page<Restaurant> restaurants = restaurantRepository.findByOwnerIdOrderByIdDesc(ownerId, pageable);
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

        Restaurant restaurant = new Restaurant();
        restaurant.setName(dto.getName());
        restaurant.setOwner(currentUser);

        Location location = new Location();
        location.setLatitud(dto.getLocationDto().getLatitud());
        location.setLongitud(dto.getLocationDto().getLongitud());
        restaurant.setLocation(location);

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

        restaurant.setName(dto.getName());

        Location location = restaurant.getLocation();
        location.setLatitud(dto.getLocationDto().getLatitud());
        location.setLongitud(dto.getLocationDto().getLongitud());

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
        dto.setOwnerName(restaurant.getOwner().getName());
        dto.setLocationDto(toLocationDto(restaurant.getLocation()));
        dto.setTotalReviews(restaurant.getValoraciones().size());
        return dto;
    }

    private RestaurantResponseDto toRestaurantResponse(Restaurant restaurant) {
        RestaurantResponseDto dto = new RestaurantResponseDto();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setOwnerId(restaurant.getOwner().getId());
        dto.setOwnerName(restaurant.getOwner().getName());
        dto.setLocationDto(toLocationDto(restaurant.getLocation()));
        dto.setTotalReviews(restaurant.getValoraciones().size());
        dto.setHasMenu(restaurant.getMenu() != null);
        return dto;
    }

    private ReviewResponseDto toReviewResponse(Review review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(review.getId());
        dto.setContent(review.getContent());
        //dto.setRating(review.getRating());
        dto.setRestaurantId(review.getRestaurant().getId());
        dto.setUserId(review.getUser().getId());
        dto.setUserName(review.getUser().getName());
        dto.setUserLastname(review.getUser().getLastname());
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
        dto.setUserLastname(comment.getUser().getLastname());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }

    private LocationDto toLocationDto(Location location) {
        LocationDto dto = new LocationDto();
        dto.setLatitud(location.getLatitud());
        dto.setLongitud(location.getLongitud());
        return dto;
    }
}