package com.demo.DBPBackend.user.domain;

import com.demo.DBPBackend.auth.domain.RecoveryCodeStore;
import com.demo.DBPBackend.auth.dto.RecoveryResponseDto;
import com.demo.DBPBackend.auth.utils.AuthUtils;
import com.demo.DBPBackend.comment.domain.Comment;
import com.demo.DBPBackend.comment.dto.CommentResponseDto;
import com.demo.DBPBackend.comment.infrastructure.CommentRepository;
import com.demo.DBPBackend.exceptions.ResourceNotFoundException;
import com.demo.DBPBackend.exceptions.UnauthorizedOperationException;
import com.demo.DBPBackend.localMediaStorage.domain.MediaStorageService;
import com.demo.DBPBackend.location.dto.LocationDto;
import com.demo.DBPBackend.restaurant.domain.Restaurant;
import com.demo.DBPBackend.restaurant.dto.RestaurantResponseDto;
import com.demo.DBPBackend.restaurant.dto.RestaurantSummaryDto;
import com.demo.DBPBackend.restaurant.infrastructure.RestaurantRepository;
import com.demo.DBPBackend.review.domain.Review;
import com.demo.DBPBackend.review.dto.ReviewResponseDto;
import com.demo.DBPBackend.review.infrastructure.ReviewRepository;
import com.demo.DBPBackend.user.dto.*;
import com.demo.DBPBackend.user.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final MediaStorageService mediaStorageService;
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final RecoveryCodeStore recoveryCodeStore;

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

    @Transactional
    public void updateUser(UserRequestDto updatedUser) {
        String email = authorizationUtils.getCurrentUserEmail();
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (updatedUser.getName() != null && !updatedUser.getName().isEmpty()) {
            existingUser.setName(updatedUser.getName());
        }

        if (updatedUser.getLastname() != null && !updatedUser.getLastname().isEmpty()) {
            existingUser.setLastname(updatedUser.getLastname());
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
            existingUser.setPassword(encodedPassword);
        }

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
            existingUser.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getPhone() != null && !updatedUser.getPhone().isEmpty()) {
            existingUser.setPhone(updatedUser.getPhone());
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

        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> favouriteRestaurantsPage = restaurantRepository.findByFavouritedBy_Id(user.getId(), pageable);

        return favouriteRestaurantsPage.map(this::toRestaurantResponseDto);
    }

    public Page<RestaurantResponseDto> getOwnedRestaurants(int page, int size) {
        String email = authorizationUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> ownedRestaurantsPage = restaurantRepository.findByOwnerId(user.getId(), pageable);

        return ownedRestaurantsPage.map(this::toRestaurantResponseDto);
    }

    public Page<CommentResponseDto> getUserComments(int page, int size) {
        String email = authorizationUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentsPage = commentRepository.findByUserId(user.getId(), pageable);

        return commentsPage.map(this::toCommentResponseDto);
    }

    public Page<ReviewResponseDto> getUserReviews(int page, int size) {
        String email = authorizationUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviewsPage = reviewRepository.findByUserId(user.getId(), pageable);

        return reviewsPage.map(this::toReviewResponseDto);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public UserResponseDto updateProfileImage(Long id, UserUpdateProfileImageDto userUpdateProfileImageDto) throws FileUploadException {
        if (!authorizationUtils.isAdminOrResourceOwner(id)) {
            throw new UnauthorizedOperationException("You are not authorized to perform this action");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        String profileImageUrl = mediaStorageService.uploadFile(userUpdateProfileImageDto.getProfileImage());
        user.setProfileImageUrl(profileImageUrl);

        userRepository.save(user);

        return toUserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateProfileImage(UserUpdateProfileImageDto userUpdateProfileImageDto) throws FileUploadException {
        String email = authorizationUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String profileImageUrl = mediaStorageService.uploadFile(userUpdateProfileImageDto.getProfileImage());
        user.setProfileImageUrl(profileImageUrl);
        userRepository.save(user);
        return toUserResponseDto(user);
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
        dto.setProfileImageUrl(user.getProfileImageUrl());
        
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

    //Metodos de busqueda adicionales:


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

    public RecoveryResponseDto updatePasswordWithCode(UserSecurityUpdateDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!recoveryCodeStore.isValid(dto.getEmail(), dto.getSecurityCode())) {
            throw new IllegalArgumentException("Código de seguridad inválido o expirado");
        }
        if (!isValidPassword(dto.getNewPassword())) {
            throw new IllegalArgumentException("La contraseña no cumple los requisitos de seguridad");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        recoveryCodeStore.invalidate(dto.getEmail());
        return new RecoveryResponseDto("Contraseña actualizada correctamente");
    }

    private boolean isValidPassword(String password) {
        // Al menos 8 caracteres, una mayúscula, un número
        return password != null && password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[0-9].*");
    }

}
