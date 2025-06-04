package com.demo.DBPBackend.user.domain;

import com.demo.DBPBackend.auth.utils.AuthUtils;
import com.demo.DBPBackend.comment.dto.CommentResponseDto;
import com.demo.DBPBackend.exceptions.ResourceNotFoundException;
import com.demo.DBPBackend.exceptions.UnauthorizedOperationException;
import com.demo.DBPBackend.restaurant.domain.Restaurant;
import com.demo.DBPBackend.restaurant.dto.RestaurantResponseDto;
import com.demo.DBPBackend.review.dto.ReviewResponseDto;
import com.demo.DBPBackend.user.dto.UserPublicUpdateDto;
import com.demo.DBPBackend.user.dto.UserRequestDto;
import com.demo.DBPBackend.user.dto.UserResponseDto;
import com.demo.DBPBackend.user.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class  UserService {

    private final UserRepository userRepository;
    private final AuthUtils authorizationUtils;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    public UserResponseDto getMe() {
        String email = authorizationUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return modelMapper.map(user, UserResponseDto.class);
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return modelMapper.map(user, UserResponseDto.class);
    }

    public List<UserResponseDto> getAllUsers() {
        if (!authorizationUtils.isAdmin())
            throw new UnauthorizedOperationException("You are not authorized to perform this action");

        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
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

        user.setName(updatedInfo.getName());
        user.setLastname(updatedInfo.getLastname());
        user.setPhone(updatedInfo.getPhone());

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

    public List<RestaurantResponseDto> getFavouriteRestaurants() {
        String email = authorizationUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return user.getFavouriteRestaurants().stream()
                .map(restaurant -> modelMapper.map(restaurant, RestaurantResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<RestaurantResponseDto> getOwnedRestaurants() {
        String email = authorizationUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return user.getOwnedRestaurants().stream()
                .map(restaurant -> modelMapper.map(restaurant, RestaurantResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<CommentResponseDto> getUserComments() {
        String email = authorizationUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return user.getComments().stream()
                .map(comment -> modelMapper.map(comment, CommentResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getUserReviews() {
        String email = authorizationUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return user.getReviews().stream()
                .map(review -> modelMapper.map(review, ReviewResponseDto.class))
                .collect(Collectors.toList());
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
