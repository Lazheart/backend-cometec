package com.demo.DBPBackend.user.domain;

import com.demo.DBPBackend.auth.utils.AuthUtils;
import com.demo.DBPBackend.comment.domain.Comment;
import com.demo.DBPBackend.comment.dto.CommentResponseDto;
import com.demo.DBPBackend.exceptions.ResourceNotFoundException;
import com.demo.DBPBackend.exceptions.UnauthorizedOperationException;
import com.demo.DBPBackend.location.dto.LocationDto;
import com.demo.DBPBackend.restaurant.domain.Restaurant;
import com.demo.DBPBackend.restaurant.dto.RestaurantResponseDto;
import com.demo.DBPBackend.restaurant.dto.RestaurantSummaryDto;
import com.demo.DBPBackend.review.domain.Review;
import com.demo.DBPBackend.review.dto.ReviewResponseDto;
import com.demo.DBPBackend.user.dto.UserPublicUpdateDto;
import com.demo.DBPBackend.user.dto.UserRequestDto;
import com.demo.DBPBackend.user.dto.UserResponseDto;
import com.demo.DBPBackend.user.dto.UserSummaryDto;
import com.demo.DBPBackend.user.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthUtils authorizationUtils;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto getMe() {
        String email = authorizationUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toUserResponseDto(user);
    }

    public UserResponseDto getUserById(Long id) {
        if (!authorizationUtils.isAdminOrResourceOwner(id))
            throw new UnauthorizedOperationException("You are not authorized to perform this action");

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toUserResponseDto(user);
    }

    public Page<UserResponseDto> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(this::toUserResponseDto);
    }

    public Page<UserResponseDto> getAllUsersOrderedByName(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAllByOrderByNameAsc(pageable);
        return userPage.map(this::toUserResponseDto);
    }

    public Page<UserResponseDto> getAllUsersOrderedByCreatedAt(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAllByOrderByCreatedAtDesc(pageable);
        return userPage.map(this::toUserResponseDto);
    }

    public Page<UserResponseDto> getAllUsersOrderedByEmail(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAllByOrderByEmailAsc(pageable);
        return userPage.map(this::toUserResponseDto);
    }

    public Page<UserResponseDto> getUsersByName(String name, int page, int size) {
        String normalizedName = normalizeText(name);
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findByNameContaining(normalizedName, pageable);
        return users.map(this::toUserResponseDto);
    }

    public Page<UserResponseDto> getUsersByLastname(String lastname, int page, int size) {
        String normalizedLastname = normalizeText(lastname);
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findByLastnameContaining(normalizedLastname, pageable);
        return users.map(this::toUserResponseDto);
    }

    public Page<UserResponseDto> getUsersByEmail(String email, int page, int size) {
        String normalizedEmail = normalizeText(email);
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findByEmailContaining(normalizedEmail, pageable);
        return users.map(this::toUserResponseDto);
    }

    public Page<UserResponseDto> getUsersByRole(Role role, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findByRole(role, pageable);
        return userPage.map(this::toUserResponseDto);
    }

    public Page<UserResponseDto> getUsersByRoleOrderedByCreatedAt(Role role, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findByRoleOrderByCreatedAtDesc(role, pageable);
        return userPage.map(this::toUserResponseDto);
    }

    public Page<UserResponseDto> getUsersByRoleOrderedByName(Role role, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findByRoleOrderByNameAsc(role, pageable);
        return userPage.map(this::toUserResponseDto);
    }

    @Transactional
    public void updateUser(UserRequestDto updatedUser) {
        String email = authorizationUtils.getCurrentUserEmail();
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (updatedUser.getName() != null && !updatedUser.getName().isEmpty()) {
            existingUser.setName(updatedUser.getName());
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
            existingUser.setPassword(encodedPassword);
        }

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
            existingUser.setEmail(updatedUser.getEmail());
        }

        userRepository.save(existingUser);
    }

    @Transactional
    public void updatePublicUserInfo(UserPublicUpdateDto updatedInfo) {
        String email = authorizationUtils.getCurrentUserEmail(); // Obtiene el email del token
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (updatedInfo.getName() != null && !updatedInfo.getName().isEmpty()) {
            user.setName(updatedInfo.getName());
        }
        
        if (updatedInfo.getLastname() != null && !updatedInfo.getLastname().isEmpty()) {
            user.setLastname(updatedInfo.getLastname());
        }
        
        if (updatedInfo.getPhone() != null && !updatedInfo.getPhone().isEmpty()) {
            user.setPhone(updatedInfo.getPhone());
        }

        userRepository.save(user);
    }

    @Transactional
    public void deleteUserById(Long id) {
        if (!authorizationUtils.isAdminOrResourceOwner(id))
            throw new UnauthorizedOperationException("You are not authorized to perform this action");

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        //Elimina al usuario de todas las relaciones antes de eliminarlo definitivamente
        for (Restaurant restaurant : user.getFavouriteRestaurants()) {
            restaurant.getFavouritedBy().remove(user);
        }

        userRepository.delete(user);
    }

    public Page<RestaurantResponseDto> getFavouriteRestaurants(int page, int size) {
        String email = authorizationUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Como no hay método directo en el repositorio, usamos stream con paginación manual
        List<RestaurantResponseDto> allFavourites = user.getFavouriteRestaurants().stream()
                .map(this::toRestaurantResponseDto)
                .collect(Collectors.toList());
        
        // Paginación manual
        int start = page * size;
        int end = Math.min(start + size, allFavourites.size());
        
        if (start >= allFavourites.size()) {
            return Page.empty(PageRequest.of(page, size));
        }
        
        List<RestaurantResponseDto> pageContent = allFavourites.subList(start, end);
        return new PageImpl<>(pageContent, PageRequest.of(page, size), allFavourites.size());
    }

    public Page<RestaurantResponseDto> getOwnedRestaurants(int page, int size) {
        String email = authorizationUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Como no hay método directo en el repositorio, usamos stream con paginación manual
        List<RestaurantResponseDto> allOwned = user.getOwnedRestaurants().stream()
                .map(this::toRestaurantResponseDto)
                .collect(Collectors.toList());
        
        // Paginación manual
        int start = page * size;
        int end = Math.min(start + size, allOwned.size());
        
        if (start >= allOwned.size()) {
            return Page.empty(PageRequest.of(page, size));
        }
        
        List<RestaurantResponseDto> pageContent = allOwned.subList(start, end);
        return new PageImpl<>(pageContent, PageRequest.of(page, size), allOwned.size());
    }

    public Page<CommentResponseDto> getUserComments(int page, int size) {
        String email = authorizationUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Como no hay método directo en el repositorio, usamos stream con paginación manual
        List<CommentResponseDto> allComments = user.getComments().stream()
                .map(this::toCommentResponseDto)
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

    public Page<ReviewResponseDto> getUserReviews(int page, int size) {
        String email = authorizationUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Como no hay método directo en el repositorio, usamos stream con paginación manual
        List<ReviewResponseDto> allReviews = user.getReviews().stream()
                .map(this::toReviewResponseDto)
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

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private UserResponseDto toUserResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLastname(user.getLastname());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        
        // Mapear reviews usando DTOs
        if (user.getReviews() != null) {
            List<ReviewResponseDto> reviewDtos = user.getReviews().stream()
                .map(this::toReviewResponseDto)
                .collect(Collectors.toList());
            dto.setReviews(reviewDtos);
        }
        
        // Mapear restaurantes favoritos usando DTOs
        if (user.getFavouriteRestaurants() != null) {
            List<RestaurantSummaryDto> restaurantDtos = user.getFavouriteRestaurants().stream()
                .map(this::toRestaurantSummaryDto)
                .collect(Collectors.toList());
            dto.setFavouriteRestaurants(restaurantDtos);
        }
        
        return dto;
    }

    private UserSummaryDto toUserSummaryDto(User user) {
        UserSummaryDto dto = new UserSummaryDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLastname(user.getLastname());
        return dto;
    }

    private RestaurantResponseDto toRestaurantResponseDto(Restaurant restaurant) {
        RestaurantResponseDto dto = new RestaurantResponseDto();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setCategory(restaurant.getCategory());
        dto.setOwnerId(restaurant.getOwner().getId());
        dto.setOwnerName(restaurant.getOwner().getName());
        
        // Mapear ubicación
        if (restaurant.getLocation() != null) {
            LocationDto locationDto = new LocationDto();
            locationDto.setLatitud(restaurant.getLocation().getLatitud());
            locationDto.setLongitud(restaurant.getLocation().getLongitud());
            dto.setLocationDto(locationDto);
        }
        
        dto.setTotalReviews(restaurant.getValoraciones().size());
        dto.setHasMenu(restaurant.getMenu() != null);
        return dto;
    }

    private CommentResponseDto toCommentResponseDto(Comment comment) {
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

    private ReviewResponseDto toReviewResponseDto(Review review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(review.getId());
        dto.setContent(review.getContent());
        dto.setRestaurantId(review.getRestaurant().getId());
        dto.setUserId(review.getUser().getId());
        dto.setUserName(review.getUser().getName());
        dto.setUserLastname(review.getUser().getLastname());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }

    private RestaurantSummaryDto toRestaurantSummaryDto(Restaurant restaurant) {
        RestaurantSummaryDto dto = new RestaurantSummaryDto();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setCategory(restaurant.getCategory());
        dto.setOwnerId(restaurant.getOwner().getId());
        dto.setOwnerName(restaurant.getOwner().getName());
        
        // Mapear ubicación
        if (restaurant.getLocation() != null) {
            LocationDto locationDto = new LocationDto();
            locationDto.setLatitud(restaurant.getLocation().getLatitud());
            locationDto.setLongitud(restaurant.getLocation().getLongitud());
            dto.setLocationDto(locationDto);
        }
        
        dto.setTotalReviews(restaurant.getValoraciones().size());
        dto.setHasMenu(restaurant.getMenu() != null);
        return dto;
    }

    private String normalizeText(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
}
