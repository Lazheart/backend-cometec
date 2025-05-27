package com.demo.DBPBackend.user.application;

import com.demo.DBPBackend.restaurant.dto.RestaurantResponseDto;
import com.demo.DBPBackend.user.domain.UserService;
import com.demo.DBPBackend.user.dto.UserRequestDto;
import com.demo.DBPBackend.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ROL_USER')")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMe() {return ResponseEntity.ok(userService.getMe());}

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping("/update/me")
    public ResponseEntity<Void> updateUser(@ModelAttribute UserRequestDto updatedUser){
        userService.updateUser(updatedUser);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/favourites")
    public ResponseEntity<List<RestaurantResponseDto>> getFavouriteRestaurants() {
        return ResponseEntity.ok(userService.getFavouriteRestaurants());
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("/owned-restaurants")
    public ResponseEntity<List<RestaurantResponseDto>> getOwnedRestaurants() {
        return ResponseEntity.ok(userService.getOwnedRestaurants());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponseDto>> getUserComments() {
        return ResponseEntity.ok(userService.getUserComments());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getUserReviews() {
        return ResponseEntity.ok(userService.getUserReviews());
    }

}
