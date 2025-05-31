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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final AuthUtils authUtils;

    public List<RestaurantSummaryDto> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(this::toRestaurantSummary)
                .collect(Collectors.toList());
    }

    public RestaurantResponseDto getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        return toRestaurantResponse(restaurant);
    }

    public List<ReviewResponseDto> getRestaurantReviews(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        return restaurant.getValoraciones().stream()
                .map(this::toReviewResponse)
                .collect(Collectors.toList());
    }

    public List<CommentResponseDto> getRestaurantComments(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        return restaurant.getValoraciones().stream()
                .flatMap(review -> review.getComments().stream())
                .map(this::toCommentResponse)
                .collect(Collectors.toList());
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
    public void createRestaurant(RestaurantRequestDto dto) {
        String email = authUtils.getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Restaurant restaurant = new Restaurant();
        restaurant.setName(dto.getName());
        restaurant.setOwner(currentUser);

        // Crear ubicación como entidad independiente
        Location location = new Location();
        location.setLatitud(dto.getLocationDto().getLatitud());
        location.setLongitud(dto.getLocationDto().getLongitud());

        restaurant.setLocation(location);

        restaurantRepository.save(restaurant);
    }

    @Transactional
    public void updateRestaurant(Long id, RestaurantRequestDto dto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        if (!authUtils.isAdminOrResourceOwner(restaurant.getOwner().getId())) {
            throw new UnauthorizedOperationException("You are not authorized to update this restaurant");
        }

        restaurant.setName(dto.getName());

        // Actualizar ubicación como entidad independiente
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
        dto.setLikes(review.getLikes());
        dto.setContent(review.getContent());

        // Convertir lista de Comment a CommentResponseDto
        dto.setComments(review.getComments().stream()
                .map(comment -> {
                    CommentResponseDto commentDto = new CommentResponseDto();
                    commentDto.setId(comment.getId());
                    commentDto.setContent(comment.getContent());
                    commentDto.setUserId(comment.getUser().getId());
                    commentDto.setReviewId(comment.getReview().getId());
                    commentDto.setCreatedAt(comment.getCreatedAt());
                    return commentDto;
                })
                .collect(Collectors.toList()));

        // Datos del dueño de la reseña
        dto.setOwner(review.getUser().getName());
        dto.setOwnerId(review.getUser().getId());
        dto.setCreatedAt(review.getCreatedAt());

        // Si necesitas los ID de usuarios que dieron like
        if (review.getLikedBy() != null) {
            dto.setLikedByUserIds(review.getLikedBy().stream()
                    .map(User::getId)
                    .collect(Collectors.toSet()));
        }

        return dto;
    }

    private CommentResponseDto toCommentResponse(com.demo.DBPBackend.comment.domain.Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setUserId(comment.getUser().getId());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }

    /*
    COnfigurar para que calcule bien las valoraciones
    private Double calculateAverageRating(Restaurant restaurant) {
        if (restaurant.getValoraciones().isEmpty()) {
            return 0.0;
        }
        return restaurant.getValoraciones().stream()
                .mapToDouble(review -> review.getLikes())
                .average()
                .orElse(0.0);
    }
*/
    private LocationDto toLocationDto(Location location) {
        LocationDto dto = new LocationDto();
        dto.setLatitud(location.getLatitud());
        dto.setLongitud(location.getLongitud());
        return dto;
    }
}